package ayds.artist.external.wikipedia.data

import ayds.artist.external.ProxyInterface
import ayds.artist.external.wikipedia.injector.WikipediaInjector
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

class ProxyWikipedia: ProxyInterface {

    private val wikiService: WikipediaTrackService = initWikipediaService()
    override fun get(artistName: String): Card{
        val articleWiki = wikiService.getInfo(artistName)
        return convertWikipediaArticleToCard(articleWiki)
    }

    private fun convertWikipediaArticleToCard(article: WikipediaArticle):Card{
        val cardToReturn: Card = when(article){
            is WikipediaArticle.WikipediaArticleWithData -> {
                Card(article.artistName, article.description, article.wikipediaURL, CardSource.WIKIPEDIA, article.wikipediaLogoURL)
            }
            is WikipediaArticle.EmptyWikipediaArticle -> Card("", "","", CardSource.WIKIPEDIA, "")
        }
        return cardToReturn
    }

    private fun initWikipediaService(): WikipediaTrackService {
        return WikipediaInjector.wikipediaTrackService
    }

}