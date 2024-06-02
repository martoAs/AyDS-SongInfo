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
    private lateinit var cardContentTextViews: List<TextView>
    private lateinit var openUrlButtons : List<Button>
    private lateinit var sourceImages : List<ImageView>

    private var uiState: CardUIState = CardUIState("", "","",false,"","")
    private val locallyStoredLogo = "https://img.icons8.com/ultraviolet/40/database.png"
    private val NO_RESULTS_MSG = "No results"

    private lateinit var presenter: MoreDetailsPresenter

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
        cardContentTextViews = listOf(findViewById(R.id.cardContent1TextPane), findViewById(R.id.cardContent2TextPane), findViewById(R.id.cardContent3TextPane))
        openUrlButtons = listOf(findViewById(R.id.openUrlButton1), findViewById(R.id.openUrlButton2), findViewById(R.id.openUrlButton3))
        sourceImages = listOf(findViewById(R.id.imageView1), findViewById(R.id.imageView2), findViewById(R.id.imageView3))
    }

    private fun initializeObservables(){
        presenter.cardObservable.subscribe{ updateUI(it) }
    }

    private fun updateUI(cards: List<CardUIState>){
        when(cards.size) {
            0 -> {
                updateUINoResults()
            }
            1 -> {
                updateUI(cards[0], 0)
            }
            2 -> {
                updateUI(cards[0], 0)
                updateUI(cards[1], 1)
            }
            3 -> {
                updateUI(cards[0], 0)
                updateUI(cards[1], 1)
                updateUI(cards[2], 2)
            }
        }
    }

    private fun updateUINoResults(){
        cardContentTextViews.first().text = Html.fromHtml(NO_RESULTS_MSG, Html.FROM_HTML_MODE_LEGACY)
    }
    private fun updateUI(uiState: CardUIState, index: Int){
        runOnUiThread {
            updateUrl(uiState.url, index)
            updateServiceLogo(uiState.imageUrl, index)
            updateBiography(uiState.contentHtml)
            updateArticleText(index)
            updateEnable(uiState.actionsEnabled, index)
        }
    }

    private fun updateUrl(urlString: String, index: Int){
        uiState = uiState.copy(url = urlString)
        updateOpenUrlButton(index, urlString)
    }

    private fun updateOpenUrlButton(index: Int, url:String) {
        openUrlButtons[index].setOnClickListener {
            triggerWebBrowsingActivity(url)
        }
    }

    private fun triggerWebBrowsingActivity(url:String) {
        val intent = Intent(Intent.ACTION_VIEW)
        if(uiState.url!=""){
            intent.setData(Uri.parse(url))
            startActivity(intent)
        }
    }

    private fun updateServiceLogo(url:String, index: Int) {
        Picasso.get().load(url).into(sourceImages[index])
    }

    private fun updateBiography(biography: String){
        uiState = uiState.copy(contentHtml = biography)
    }

    private fun updateArticleText(index: Int) {
        cardContentTextViews[index].text = Html.fromHtml(uiState.contentHtml, Html.FROM_HTML_MODE_LEGACY)
    }

    private fun updateEnable(isEnabled: Boolean, index: Int){
        uiState = uiState.copy(actionsEnabled = isEnabled)
        runOnUiThread {
            openUrlButtons[index].isEnabled = uiState.actionsEnabled
        }
    }

    private fun notifyPresenter(){
        Thread{
            presenter.updateCards(getArtist())
        }.start()
    }

    private fun getArtist() = intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}
