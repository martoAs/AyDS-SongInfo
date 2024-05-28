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
import ayds.songinfo.moredetails.injector.MoreDetailsInjector
import com.squareup.picasso.Picasso

class MoreDetailsView():Activity() {
    private lateinit var cardContentTextView: TextView
    private lateinit var openUrlButton : Button
    private lateinit var sourceImage : ImageView
    private lateinit var presenter: MoreDetailsPresenter
    private lateinit var sourceLabels: List<TextView>

    private var uiState: CardUIState = CardUIState("", "","",false,"","")

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

    private fun makeLabelList() {
        val lastFMSource = findViewById<TextView>(R.id.source1)
        val wikipediaSource = findViewById<TextView>(R.id.source2)
        val newYorkTimesSource = findViewById<TextView>(R.id.source3)
        sourceLabels = listOf(lastFMSource, wikipediaSource, newYorkTimesSource)
    }

    private fun initializeViewProperties() {
        setContentView(R.layout.activity_other_info)
        cardContentTextView = findViewById(R.id.cardContent1TextPane)
        openUrlButton = findViewById(R.id.openUrlButton)
        sourceImage = findViewById(R.id.lastFMImageView)
        makeLabelList()
    }

    private fun initializeObservables(){
        presenter.cardObservable.subscribe{ updateUI(it) }
    }

    private fun updateUI(uiState: CardUIState){
        runOnUiThread {
            updateUrl(uiState.url)
            updateLastFMLogo(uiState.imageUrl)
            updateBiography(uiState.contentHtml)
            updateArticleText()
            updateEnable(uiState.actionsEnabled)
            updateSourceLabel(uiState.source)
        }
    }

    private fun updateSourceLabel(source: String) {
        sourceLabels.first().text = source
        sourceLabels.first().visibility = TextView.VISIBLE

    }

    private fun updateUrl(urlString: String){
        uiState = uiState.copy(url = urlString)
        updateOpenUrlButton()
    }

    private fun updateOpenUrlButton() {
        openUrlButton.setOnClickListener {
            triggerWebBrowsingActivity()
        }
    }

    private fun triggerWebBrowsingActivity() {
        val intent = Intent(Intent.ACTION_VIEW)
        if(uiState.url!=""){
            intent.setData(Uri.parse(uiState.url))
            startActivity(intent)
        }
    }

    private fun updateLastFMLogo(url:String) {
        Picasso.get().load(url).into(sourceImage)
    }

    private fun updateBiography(biography: String){
        uiState = uiState.copy(contentHtml = biography)
    }

    private fun updateArticleText() {
        cardContentTextView.text = Html.fromHtml(uiState.contentHtml, Html.FROM_HTML_MODE_LEGACY)
    }

    private fun updateEnable(isEnabled: Boolean){
        uiState = uiState.copy(actionsEnabled = isEnabled)
        runOnUiThread {
            openUrlButton.isEnabled = uiState.actionsEnabled
        }
    }

    private fun notifyPresenter(){
        Thread{
            presenter.updateCard(getArtist())
        }.start()
    }

    private fun getArtist() = intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}
