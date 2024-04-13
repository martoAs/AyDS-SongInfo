package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room.databaseBuilder
import ayds.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale

class OtherInfoWindow : Activity() {
    private var artistInfoDisplayer: TextView? = null

    //private JPanel imagePanel;
    // private JLabel posterImageLabel;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        artistInfoDisplayer = findViewById(R.id.textPane1)
        open(intent.getStringExtra("artistName"))
    }

    private fun getARtistInfo(artistName: String?) {

        // create
        val retrofit = getRetrofit()
        val lastFMAPI = getLastFMAPI(retrofit)
        Log.e("TAG", "artistName $artistName")
        Thread {
            val article = getArticle(artistName)
            var artistInformation = ""
            if (article != null) { // exists in db
                artistInformation = "[*]" + article.biography
                val urlString = article.articleUrl
                triggerWebBrowsingActivity(urlString)

            } else { // get from service
                artistInformation = getArtistInfoFromService(lastFMAPI, artistName, artistInformation)
              }
            val imageUrl =
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
            Log.e("TAG", "Get Image from $imageUrl")
            updateUserInterface(artistInformation, imageUrl)
        }.start()
    }

    private fun getArticle(artistName: String?) =
        dataBase!!.ArticleDao().getArticleByArtistName(artistName!!)

    private fun updateUserInterface(artistInformation: String, imageUrl: String) {
        runOnUiThread {
            Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView1) as ImageView)
            artistInfoDisplayer!!.text = Html.fromHtml(artistInformation)
        }
    }

    private fun getLastFMAPI(retrofit: Retrofit): LastFMAPI =
        retrofit.create(LastFMAPI::class.java)

    private fun getRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://ws.audioscrobbler.com/2.0/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private fun getArtistInfoFromService(
        lastFMAPI: LastFMAPI,
        artistName: String?,
        artistInformation: String
    ): String {
        var artistInformation1 = artistInformation
        val callResponse: Response<String>
        try {
            callResponse = artistName?.let { lastFMAPI.getArtistInfo(it).execute() }!!
            Log.e("TAG", "JSON " + callResponse.body())
            val jobj = jsonObject(callResponse)
            val artist = getArtist(jobj)
            val bio = getBio(artist)
            val extract = getExtract(bio)
            val url = getUrl(artist)
            artistInformation1 = extract?.asString?.replace("\\n", "\n")?.let {
                textToHtml(it, artistName).also { info ->
                    url?.let { url -> saveInformationToBD(info, artistName, url) }
                }
            } ?: "No Results"

            url?.let { triggerWebBrowsingActivity(url.asString) }

        } catch (e1: IOException) {
            Log.e("TAG", "Error $e1")
            e1.printStackTrace()
        }
        return artistInformation1
    }

    private fun getUrl(artist: JsonObject?) = artist?.get("url")

    private fun getExtract(bio: JsonObject?) = bio?.get("content")

    private fun getBio(artist: JsonObject?) =
        artist?.get("bio")?.getAsJsonObject()

    private fun getArtist(jobj: JsonObject?) = jobj?.get("artist")?.getAsJsonObject()

    private fun jsonObject(callResponse: Response<String>): JsonObject? {
        val gson = Gson()
        return gson.fromJson(callResponse.body(), JsonObject::class.java)
    }

    private fun triggerWebBrowsingActivity(url: String) {
        findViewById<View>(R.id.openUrlButton1).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(url))
            startActivity(intent)
        }
    }

    private fun saveInformationToBD(
        artistInformation: String,
        artistName: String,
        url: JsonElement
    ) {
        Thread {
            dataBase!!.ArticleDao().insertArticle(
                ArticleEntity(
                    artistName, artistInformation, url.asString
                )
            )
        }
            .start()
    }

    private var dataBase: ArticleDatabase? = null
    private fun open(artist: String?) {
        dataBase =
            databaseBuilder(this, ArticleDatabase::class.java, "database-name-thename").build()
        Thread {
            dataBase!!.ArticleDao().insertArticle(ArticleEntity("test", "sarasa", ""))
            Log.e("TAG", "" + dataBase!!.ArticleDao().getArticleByArtistName("test"))
            Log.e("TAG", "" + dataBase!!.ArticleDao().getArticleByArtistName("nada"))
        }.start()
        getARtistInfo(artist)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        fun textToHtml(text: String, term: String?): String {
            val builder = StringBuilder()
            builder.append("<html><div width=400>")
            builder.append("<font face=\"arial\">")
            val textWithBold = text
                .replace("'", " ")
                .replace("\n", "<br>")
                .replace(
                    "(?i)$term".toRegex(),
                    "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
                )
            builder.append(textWithBold)
            builder.append("</font></div></html>")
            return builder.toString()
        }
    }
}
