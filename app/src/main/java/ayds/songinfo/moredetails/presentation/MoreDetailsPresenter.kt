package ayds.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.Article.ArtistArticle
import ayds.songinfo.moredetails.domain.Article.EmptyArticle
import ayds.songinfo.moredetails.domain.ArtistArticleRepository

interface MoreDetailsPresenter{
    val artistBiographyObservable: Observable<String>
    val articleUrlObservable: Observable<String>
    val actionsEnabled: Observable<Boolean>
    fun notifyOpenArticle(artistName: String?)
}

class MoreDetailsPresenterImpl(private val repository: ArtistArticleRepository):
    MoreDetailsPresenter {
    override val articleUrlObservable = Subject<String>()
    override val artistBiographyObservable = Subject<String>()
    override val actionsEnabled = Subject<Boolean>()
    override fun notifyOpenArticle(artistName: String?){
        Thread {
            getArticleFromRepository(artistName)
        }.start()
    }

    private fun getArticleFromRepository(artistName: String?){
        artistName?.let{
            when(val article = repository.getArticleByArtistName(artistName)){
                is ArtistArticle -> {notifyArticle(article)}
                is EmptyArticle -> {notifyEmptyArticle()}
            }
        }?: notifyEmptyArticle()
    }

    private fun notifyArticle(article: ArtistArticle){
        articleUrlObservable.notify(article.articleUrl)
        artistBiographyObservable.notify(article.biography)
        actionsEnabled.notify(true)
    }

    private fun notifyEmptyArticle(){
        articleUrlObservable.notify("")
        artistBiographyObservable.notify("No results")
        actionsEnabled.notify(false)
    }


}