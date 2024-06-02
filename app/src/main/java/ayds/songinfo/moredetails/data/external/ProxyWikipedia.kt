package ayds.songinfo.moredetails.data.external

import ayds.artist.external.wikipedia.data.WikipediaArticle
import ayds.artist.external.wikipedia.data.WikipediaTrackService
import ayds.artist.external.wikipedia.injector.WikipediaInjector
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

class ProxyWikipedia: ProxyInterface {

    private val wikiService: WikipediaTrackService = initWikipediaService()
    override fun get(artistName: String): Card {
        val articleWiki = wikiService.getInfo(artistName)
        return convertWikipediaArticleToCard(articleWiki)
    }

    private fun convertWikipediaArticleToCard(article: WikipediaArticle): Card {
        val cardToReturn: Card = when(article){
            is WikipediaArticle.WikipediaArticleWithData -> {
                Card(
                    article.artistName,
                    article.description,
                    article.wikipediaURL,
                    CardSource.WIKIPEDIA,
                    article.wikipediaLogoURL
                )
            }
            is WikipediaArticle.EmptyWikipediaArticle -> Card("", "", "", CardSource.WIKIPEDIA, "")
        }
        return cardToReturn
    }

    private fun initWikipediaService(): WikipediaTrackService {
        return WikipediaInjector.wikipediaTrackService
    }

}