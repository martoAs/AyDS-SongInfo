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
import ayds.songinfo.moredetails.fulllogic.presentation.MoreDetailsUIState.Companion.LASTFM_IMAGE
import com.squareup.picasso.Picasso

class MoreDetailsView():Activity() {
    private lateinit var artistInfoDisplayer: TextView
    private lateinit var openUrlButton : Button
    private lateinit var lastFMImageView : ImageView
    private var UIState: MoreDetailsUIState = MoreDetailsUIState("", "")
    private lateinit var injector: MoreDetailsInjector
    private lateinit var presenter: MoreDetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initializeComponents()
        initializeViewProperties()
        initializeObservables()
        notifyPresenter()
    }

    private fun initializeComponents() { //GOOD
        MoreDetailsInjector.init(this)
        presenter = injector.getMoreDetailsPresenter()
    }

    private fun initializeViewProperties() { //GOOD
        setContentView(R.layout.activity_other_info)
        artistInfoDisplayer = findViewById(R.id.textPane1)
        openUrlButton = findViewById(R.id.openUrlButton)
        lastFMImageView = findViewById(R.id.lastFMImageView)
    }

    private fun initializeObservables(){ //GOOD
        presenter.artistBiographyObservable.subscribe{ updateBiography(it) }
        presenter.articleUrlObservable.subscribe{ updateUrl(it)}
    }

    private fun updateBiography(biography: String){ //GOOD
        UIState = UIState.copy(articleBiography = biography)
        updateUserInterface()
    }
    private fun updateUrl(urlString: String){
        UIState = UIState.copy(articleURL = urlString)
        updateOpenUrlButton()
    }

    private fun notifyPresenter(){
        val artist = getArtist()
        artist?.let { presenter.notifyOpenArticle(it) }
    }

    private fun getArtist() = intent.getStringExtra(ARTIST_NAME_EXTRA)

    private fun updateUserInterface() {
        runOnUiThread {
            updateLastFMLogo()
            updateArticleText()
        }
    }

    private fun updateOpenUrlButton() {
        openUrlButton.setOnClickListener {
            triggerWebBrowsingActivity()
        }
    }

    private fun triggerWebBrowsingActivity() {
        val intent = Intent(Intent.ACTION_VIEW)
        if(UIState.articleURL!=""){
            intent.setData(Uri.parse(UIState.articleURL))
            startActivity(intent)
        }
    }

    private fun updateLastFMLogo() {
        Picasso.get().load(LASTFM_IMAGE).into(lastFMImageView)
    }

    private fun updateArticleText() {
        artistInfoDisplayer.text = Html.fromHtml(UIState.articleBiography, Html.FROM_HTML_MODE_LEGACY)
    }



    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}
