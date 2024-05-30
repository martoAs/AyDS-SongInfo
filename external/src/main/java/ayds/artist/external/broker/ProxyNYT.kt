package ayds.artist.external.broker

import ayds.artist.external.newyorktimes.data.NYTimesArticle
import ayds.artist.external.newyorktimes.data.NYTimesService
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

internal class ProxyNYTImpl(private val NYTService: NYTimesService):ProxyInterface{
    override fun get(artistName: String): Card {
        val NYTimesArticle = NYTService.getArtistInfo(artistName)
        return convertLastFMArticleToCard(NYTimesArticle)
    }

    private fun convertLastFMArticleToCard(articleNYT: NYTimesArticle): Card {
        val cardToReturn: Card = when(articleNYT){
            is NYTimesArticle.NYTimesArticleWithData -> {
                Card(articleNYT.name, articleNYT.info, articleNYT.url, CardSource.NYTIMES, articleNYT.logourl)
            }
            is NYTimesArticle.EmptyArtistDataExternal -> Card("", "","", CardSource.NYTIMES, "")
        }
        return cardToReturn
    }

}