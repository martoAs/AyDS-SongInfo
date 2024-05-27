package ayds.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.OtherInfoRepository

interface MoreDetailsPresenter {
    val cardObservable: Observable<MoreDetailsUIState>
    fun updateCard(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private val repository: OtherInfoRepository,
    private val articleBiographyHelper: ArticleBiographyHelper
) :
    MoreDetailsPresenter {

    override val cardObservable = Subject<MoreDetailsUIState>()
    override fun updateCard(artistName: String) {
        val article = repository.getCard(artistName)
        cardObservable.notify(article.toUIState())
    }

    private fun Card.toUIState() = MoreDetailsUIState(
        artistName,
        articleBiographyHelper.getDescription(this),
        infoUrl,
        infoUrl != "",
        sourceLogoUrl,
        source.name

    )

}