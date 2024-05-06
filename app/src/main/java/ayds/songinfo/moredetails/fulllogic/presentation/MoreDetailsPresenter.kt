package ayds.songinfo.moredetails.fulllogic.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.fulllogic.domain.Article.ArtistArticle
import ayds.songinfo.moredetails.fulllogic.domain.Article.EmptyArticle
import ayds.songinfo.moredetails.fulllogic.domain.ArtistArticleRepository

interface MoreDetailsPresenter{
    val artistBiographyObservable: Observable<String>
    val articleUrlObservable: Observable<String>
    fun notifyOpenArticle(artistName: String?)
}

class MoreDetailsPresenterImpl(private val repository: ArtistArticleRepository): MoreDetailsPresenter{
    override val articleUrlObservable = Subject<String>()
    override val artistBiographyObservable = Subject<String>()
    override fun notifyOpenArticle(artistName: String?){
        Thread {
            getArticleFromRepository(artistName)
        }.start()
    }

    private fun getArticleFromRepository(artistName: String?){
        artistName?.let{
            when(val article = repository.getArticleByArtistName(artistName)){
                is ArtistArticle -> {articleUrlObservable.notify(article.articleUrl)
                                     artistBiographyObservable.notify(article.biography)}
                is EmptyArticle -> {articleUrlObservable.notify("")
                                     artistBiographyObservable.notify("No results")}
            }
        }
    }

}