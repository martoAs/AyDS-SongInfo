package ayds.songinfo.moredetails.data.external

import ayds.artist.external.newyorktimes.data.NYTimesArticle
import ayds.artist.external.newyorktimes.data.NYTimesService
import ayds.artist.external.newyorktimes.injector.NYTimesInjector
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

class ProxyNYTImpl: ProxyInterface {
    private val nyTimesService: NYTimesService = initNYTimesService()
    override fun get(artistName: String): Card {
        val nyTimesArticle = nyTimesService.getArtistInfo(artistName)
        return convertLastFMArticleToCard(nyTimesArticle)
    }

    private fun convertLastFMArticleToCard(articleNYT: NYTimesArticle): Card {
        val cardToReturn: Card = when(articleNYT){
            is NYTimesArticle.NYTimesArticleWithData -> {
                Card(articleNYT.name, articleNYT.info, articleNYT.url, CardSource.NYTIMES, articleNYT.logourl)
            }
            is NYTimesArticle.EmptyNYTimesArticle -> Card("", "","", CardSource.NYTIMES, "")
        }
        return cardToReturn
    }

    private fun initNYTimesService(): NYTimesService {
        return NYTimesInjector.nyTimesService
    }

}