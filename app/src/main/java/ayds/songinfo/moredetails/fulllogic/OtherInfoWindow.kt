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
    private var dataBase: ArticleDatabase? = null

    private val LASTFM_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    private val AUDIOSCROBBLER_PATH = "https://ws.audioscrobbler.com/2.0/"

    private var ARTIST_NAME: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initializeArtistName()
        initializeArtistInfoDisplayer()
        buildDatabase()
        getARtistInfo()
    }

    private fun initializeArtistName(){ ARTIST_NAME = intent.getStringExtra(ARTIST_NAME_EXTRA) }
    private fun initializeArtistInfoDisplayer() { artistInfoDisplayer = findViewById(R.id.textPane1) }
    private fun buildDatabase(){ dataBase = databaseBuilder(this, ArticleDatabase::class.java, "database-name-thename").build() }
    private fun getARtistInfo() {
        Log.e("TAG", "artistName $ARTIST_NAME")
        Thread {
            var artistInformation = ""
            getArticle()?.let {
                artistInformation = "[*]" + it.biography
                triggerWebBrowsingActivity()
            } ?: run {
                artistInformation = getArtistInfoFromService(artistInformation)
            }
            Log.e("TAG", "Get Image from $LASTFM_IMAGE")
            updateUserInterface(artistInformation)
        }.start()
    }

    private fun getArticle() = ARTIST_NAME?.let { dataBase!!.ArticleDao().getArticleByArtistName(it) }

    private fun updateUserInterface(artistInformation: String) {
        Log.e("TAG", "thread artistInfo $artistInformation")
        runOnUiThread {
            Picasso.get().load(LASTFM_IMAGE).into(findViewById<View>(R.id.imageView1) as ImageView)
            artistInfoDisplayer!!.text = Html.fromHtml(artistInformation)
        }
    }

    private fun getLastFMAPI(): LastFMAPI = buildRetrofit().create(LastFMAPI::class.java)
    private fun buildRetrofit(): Retrofit = Retrofit.Builder().baseUrl(AUDIOSCROBBLER_PATH).addConverterFactory(ScalarsConverterFactory.create()).build()
    private fun getArtistInfoFromService(artistInformation: String): String {
        var artistInfo = artistInformation
        val callResponse: Response<String>
        try {
            callResponse = ARTIST_NAME?.let { getLastFMAPI().getArtistInfo(it).execute() }!!
            Log.e("TAG", "JSON " + callResponse.body())
            ArtistJSON.setJSON(callResponse)

            Log.e("TAG", "JSON EXTRACT " + ArtistJSON.getExtract())
            artistInfo = ArtistJSON.getExtract()?.asString?.replace("\\n", "\n")?.let {
                textToHtml(it, ARTIST_NAME).also { info ->
                    ArtistJSON.getUrl()?.let { url -> saveInformationToBD(info, ARTIST_NAME!!, url) }
                }
            } ?: "No Results"

            ArtistJSON.getUrl()?.let { triggerWebBrowsingActivity() }

        } catch (e1: IOException) {
            Log.e("TAG", "Error $e1")
            e1.printStackTrace()
        }
        return artistInfo
    }

    private fun triggerWebBrowsingActivity() {
        findViewById<View>(R.id.openUrlButton1).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(ArtistJSON.getUrl()?.asString))
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

    object ArtistJSON{
        private var artistJSON = JsonObject()
        fun setJSON(callResponse: Response<String>){
            val gson = Gson()
            artistJSON = gson.fromJson(callResponse.body(), JsonObject::class.java)
        }
        fun getUrl() = artistJSON.get("url")
        fun getExtract() = getBio()?.get("content")
        private fun getBio() = getArtist()?.get("bio")?.getAsJsonObject()
        private fun getArtist() = artistJSON.get("artist")?.getAsJsonObject()
    }
}
