package ayds.songinfo.moredetails.fulllogic.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import ayds.songinfo.R
import ayds.songinfo.moredetails.fulllogic.dependency_injector.MoreDetailsInjector
import ayds.songinfo.moredetails.fulllogic.domain.Article.ArtistArticle
import ayds.songinfo.moredetails.fulllogic.presentation.MoreDetailsUIState.Companion.LASTFM_IMAGE
import com.squareup.picasso.Picasso
import java.util.Locale

class MoreDetailsView():Activity() {
    private lateinit var artistInfoDisplayer: TextView
    private lateinit var openUrlButton : Button
    private lateinit var lastFMImageView : ImageView
    private var UIState: MoreDetailsUIState = MoreDetailsUIState("")
    private lateinit var injector: MoreDetailsInjector
    private lateinit var presenter: MoreDetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MoreDetailsInjector.init(this)
        presenter = injector.getMoreDetailsPresenter()
        setContentView(R.layout.activity_other_info)

        initializeViewProperties()
        getArtistArticleAsync()
    }

    private fun initializeViewProperties() {
        artistInfoDisplayer = findViewById(R.id.textPane1)
        openUrlButton = findViewById(R.id.openUrlButton)
        lastFMImageView = findViewById(R.id.lastFMImageView)
    }

    private fun getArtistArticleAsync() {
        Thread {
            getArtistArticle()
        }.start()
    }

    private fun getArtistArticle(){
        val newState = presenter.notifyOpenArticle(MoreDetailsUIEvent.openArticle, intent.getStringExtra(ARTIST_NAME_EXTRA))
        updateState(newState)
        updateUserInterface()
    }

    private fun updateUserInterface(artistArticle: ArtistArticle) {
        runOnUiThread {
            updateOpenUrlButton()
            updateLastFMLogo()
            updateArticleText(artistArticle)
        }
    }

    private fun updateOpenUrlButton() {
       openUrlButton.setOnClickListener {
            triggerWebBrowsingActivity()
        }
    }

    private fun updateLastFMLogo() {
        Picasso.get().load(LASTFM_IMAGE).into(lastFMImageView)
    }

    private fun updateArticleText(artistArticle: ArtistArticle) {
        val text = artistArticle.biography.replace("\\n", "\n")
        artistInfoDisplayer.text = Html.fromHtml(textToHtml(text, artistArticle.artistName))
    }

    private fun triggerWebBrowsingActivity() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(UIState.articleURL))
        startActivity(intent)
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

    private fun updateState(newState:MoreDetailsUIState){
        UIState = UIState.copy( articleURL = newState.articleURL )
    }
    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}
