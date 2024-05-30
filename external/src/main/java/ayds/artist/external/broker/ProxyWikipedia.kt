package ayds.artist.external.broker

import ayds.artist.external.wikipedia.data.WikipediaArticle
import ayds.artist.external.wikipedia.data.WikipediaTrackService
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

internal class ProxyWikipedia(private val wikiService: WikipediaTrackService): ProxyInterface{
    override fun get(artistName: String): Card{
        var articleWiki = wikiService.getInfo(artistName)
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

}