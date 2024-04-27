package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room.databaseBuilder
import ayds.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale



private const val LASTFM_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
private const val AUDIOSCROBBLER_PATH = "https://ws.audioscrobbler.com/2.0/"
private const val ARTICLE_BDNAME = "database-article"

data class ArtistBiography(val artistName: String, val biography: String, val articleUrl: String)

class OtherInfoWindow : Activity() {
    private lateinit var artistInfoDisplayer: TextView
    private lateinit var articleDatabase: ArticleDatabase
    private lateinit var openUrlButton : Button
    private lateinit var lastFMAPI : LastFMAPI
    private lateinit var lastFMImageView : ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initializeViewProperties()
        buildDatabase()
        initLastFMAPI()
        getArtistInfoAsync()
    }


    private fun initializeViewProperties() {
        artistInfoDisplayer = findViewById(R.id.textPane1)
        openUrlButton = findViewById(R.id.openUrlButton)
        lastFMImageView = findViewById(R.id.lastFMImageView)
    }
    private fun buildDatabase(){ articleDatabase = databaseBuilder(this, ArticleDatabase::class.java, ARTICLE_BDNAME).build() }

    private fun initLastFMAPI() {
        val retrofit = Retrofit.Builder()
            .baseUrl(AUDIOSCROBBLER_PATH)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        lastFMAPI = retrofit.create(LastFMAPI::class.java)
    }

    private fun getArtistInfoAsync() {
        Thread {
            getArtistInfo()
        }.start()
    }

    private fun getArtistInfo() {
        val artistBiography = getArtistInfoFromRepository()
        updateUserInterface(artistBiography)
    }

    private fun getArtistInfoFromRepository(): ArtistBiography {
        val artistName = getArtistName()

        val databaseArticle = getArticleFromDB(artistName)

        val artistBiography: ArtistBiography

        if (databaseArticle != null) {
            artistBiography = databaseArticle.markItAsLocal()
        } else {
            artistBiography = getArticleFromService(artistName)
            if (artistBiography.biography.isNotEmpty()) {
                saveInformationToBD(artistBiography)
            }
        }
        return artistBiography
    }

    private fun ArtistBiography.markItAsLocal() = copy(biography = "[*]$biography")

    private fun getArticleFromDB(artistName: String): ArtistBiography? {
        val artistEntity = articleDatabase.ArticleDao().getArticleByArtistName(artistName)
        return artistEntity?.let {
            ArtistBiography(artistName, artistEntity.biography, artistEntity.articleUrl)
        }
    }

    private fun getArticleFromService(artistName: String): ArtistBiography {

        var artistBiography = ArtistBiography(artistName, "", "")
        try {
            val callResponse = getSongFromService(artistName)
            artistBiography = getArtistBioFromExternalData(callResponse.body(), artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return artistBiography
    }
    private fun getText(extract: JsonElement?) = extract?.asString ?: "No Results"
    private fun getUrl(artist: JsonObject): JsonElement = artist["url"]
    private fun getExtract(bio: JsonObject): JsonElement? = bio["content"]
    private fun getBio(artist: JsonObject): JsonObject = artist["bio"].getAsJsonObject()
    private fun getArtist(jobj: JsonObject): JsonObject = jobj["artist"].getAsJsonObject()
    private fun getArtistBioFromExternalData(
        serviceData: String?,
        artistName: String
    ): ArtistBiography {
        val gson = Gson()
        val jobj = gson.fromJson(serviceData, JsonObject::class.java)

        val artist = getArtist(jobj)
        val bio = getBio(artist)
        val extract = getExtract(bio)
        val url = getUrl(artist)
        val text = getText(extract)

        return ArtistBiography(artistName, text, url.asString)
    }




    private fun getSongFromService(artistName: String) =
        lastFMAPI.getArtistInfo(artistName).execute()

    private fun saveInformationToBD(artistBiography: ArtistBiography) {
        articleDatabase.ArticleDao().insertArticle(
            ArticleEntity(
                artistBiography.artistName, artistBiography.biography, artistBiography.articleUrl
            )
        )
    }

    private fun updateUserInterface(artistBiography: ArtistBiography) {
        runOnUiThread {
            updateOpenUrlButton(artistBiography)
            updateLastFMLogo()
            updateArticleText(artistBiography)
        }
    }

    private fun updateOpenUrlButton(artistBiography: ArtistBiography) {
       openUrlButton.setOnClickListener {
            triggerWebBrowsingActivity(artistBiography.articleUrl)
        }
    }

    private fun triggerWebBrowsingActivity(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }

    private fun updateLastFMLogo() {
        Picasso.get().load(LASTFM_IMAGE).into(lastFMImageView)
    }

    private fun getArtistName() =
        intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

    private fun updateArticleText(artistBiography: ArtistBiography) {
        val text = artistBiography.biography.replace("\\n", "\n")
        artistInfoDisplayer.text = Html.fromHtml(textToHtml(text, artistBiography.artistName))
    }

    private fun textToHtml(text: String, term: String?): String {
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

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}
