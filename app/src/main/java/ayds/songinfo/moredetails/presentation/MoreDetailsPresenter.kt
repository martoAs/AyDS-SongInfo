package ayds.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.ArtistArticleRepository

interface MoreDetailsPresenter {
    val articleObservable: Observable<MoreDetailsUIState>
    fun notifyOpenArticle(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private val repository: ArtistArticleRepository,
    private val articleBiographyHelper: ArticleBiographyHelper
) :
    MoreDetailsPresenter {

    override val articleObservable = Subject<MoreDetailsUIState>()
    override fun notifyOpenArticle(artistName: String) {
        val article = repository.getArticleByArtistName(artistName)
        articleObservable.notify(article.toUIState())
    }

    private fun Card.toUIState() = MoreDetailsUIState(
        artistName,
        articleBiographyHelper.getDescription(this),
        infoUrl,
        infoUrl != "",
        sourceLogoUrl
    )

}