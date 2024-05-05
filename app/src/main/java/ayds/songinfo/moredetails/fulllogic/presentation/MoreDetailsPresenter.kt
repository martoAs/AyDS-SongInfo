package ayds.songinfo.moredetails.fulllogic.presentation

import ayds.songinfo.moredetails.fulllogic.dependency_injector.MoreDetailsInjector
import ayds.songinfo.moredetails.fulllogic.domain.Article.ArtistArticle
import ayds.songinfo.moredetails.fulllogic.domain.Article.EmptyArticle
import ayds.songinfo.moredetails.fulllogic.domain.ArtistArticleRepository

interface MoreDetailsPresenter{
    fun notifyOpenArticle(event: MoreDetailsUIEvent, artistName: String?):MoreDetailsUIState
}

class MoreDetailsPresenterImpl(private val repository: ArtistArticleRepository): MoreDetailsPresenter{
    private lateinit var UIState: MoreDetailsUIState
    private val MoreDetailsInjector : MoreDetailsInjector

    override fun notifyOpenArticle(event: MoreDetailsUIEvent, artistName: String?):MoreDetailsUIState{
        artistName?.let{
            val article = repository.getArticleByArtistName(artistName)
            when(article){
                is ArtistArticle -> UIState = MoreDetailsUIState(article.articleUrl)
                is EmptyArticle -> UIState = MoreDetailsUIState("")
            }
        }
        return UIState
    }


}