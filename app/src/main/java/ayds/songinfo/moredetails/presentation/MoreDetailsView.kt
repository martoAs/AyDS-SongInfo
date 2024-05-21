package ayds.songinfo.moredetails.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import ayds.songinfo.R
import ayds.songinfo.moredetails.dependency_injector.MoreDetailsInjector
import com.squareup.picasso.Picasso

class MoreDetailsView():Activity() {
    private lateinit var artistInfoDisplayer: TextView
    private lateinit var openUrlButton : Button
    private lateinit var lastFMImageView : ImageView
    private lateinit var presenter: MoreDetailsPresenter

    private var UIState: MoreDetailsUIState = MoreDetailsUIState("", "","",false,"")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeComponents()
        initializeViewProperties()
        initializeObservables()
        notifyPresenter()
    }

    private fun initializeComponents() {
        MoreDetailsInjector.init(this)
        presenter = MoreDetailsInjector.getMoreDetailsPresenter()
    }

    private fun initializeViewProperties() {
        setContentView(R.layout.activity_other_info)
        artistInfoDisplayer = findViewById(R.id.textPane1)
        openUrlButton = findViewById(R.id.openUrlButton)
        lastFMImageView = findViewById(R.id.lastFMImageView)
    }

    private fun initializeObservables(){
        presenter.articleObservable.subscribe{ updateUI(it) }
    }

    private fun updateUI(uiState: MoreDetailsUIState){
        runOnUiThread {
            updateUrl(uiState.articleURL)
            updateLastFMLogo(uiState.imageUrl)
            updateBiography(uiState.articleBiography)
            updateArticleText()
            updateEnable(uiState.actionsEnabled)
        }
    }

    private fun updateUrl(urlString: String){
        UIState = UIState.copy(articleURL = urlString)
        updateOpenUrlButton()
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

    private fun updateLastFMLogo(url:String) {
        Picasso.get().load(url).into(lastFMImageView)
    }

    private fun updateBiography(biography: String){
        UIState = UIState.copy(articleBiography = biography)
    }

    private fun updateArticleText() {
        artistInfoDisplayer.text = Html.fromHtml(UIState.articleBiography, Html.FROM_HTML_MODE_LEGACY)
    }

    private fun updateEnable(isEnabled: Boolean){
        UIState = UIState.copy(actionsEnabled = isEnabled)
        runOnUiThread {
            openUrlButton.isEnabled = UIState.actionsEnabled
        }
    }

    private fun notifyPresenter(){
        Thread{
            presenter.notifyOpenArticle(getArtist())
        }.start()
    }

    private fun getArtist() = intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}
