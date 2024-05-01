package ayds.songinfo.moredetails.fulllogic.presentation

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
import ayds.songinfo.moredetails.fulllogic.LastFMAPI

import ayds.songinfo.moredetails.fulllogic.data.ArticleDatabase

import ayds.songinfo.moredetails.fulllogic.domain.Article.ArtistArticle

import ayds.songinfo.moredetails.fulllogic.domain.ArtistArticleRepository

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.Locale

private const val LASTFM_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
private const val AUDIOSCROBBLER_PATH = "https://ws.audioscrobbler.com/2.0/"
private const val ARTICLE_BDNAME = "database-article"

class MoreDetailsView(private val repository: ArtistArticleRepository) : Activity() {
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
        val artistArticle = repository.getArticleByArtistName(intent.getStringExtra(ARTIST_NAME_EXTRA))
        updateUserInterface(artistArticle)
    }

    private fun updateUserInterface(artistArticle: ArtistArticle) {
        runOnUiThread {
            updateOpenUrlButton(artistArticle)
            updateLastFMLogo()
            updateArticleText(artistArticle)
        }
    }

    private fun updateOpenUrlButton(artistArticle: ArtistArticle) {
       openUrlButton.setOnClickListener {
            triggerWebBrowsingActivity(artistArticle.articleUrl)
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

    private fun updateArticleText(artistArticle: ArtistArticle) {
        val text = artistArticle.biography.replace("\\n", "\n")
        artistInfoDisplayer.text = Html.fromHtml(textToHtml(text, artistArticle.artistName))
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
