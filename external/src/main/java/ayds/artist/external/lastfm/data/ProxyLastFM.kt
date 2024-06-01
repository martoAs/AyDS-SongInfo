package ayds.artist.external.lastfm.data

import ayds.artist.external.ProxyInterface
import ayds.artist.external.lastfm.injector.LastFMInjector
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

class ProxyLastFMImpl: ProxyInterface {

    private val lastFMService: LastFMService = initLastFMService()
    override fun get(artistName: String):Card{
        val lastFMArticle = lastFMService.getArticle(artistName)
        return convertLastFMArticleToCard(lastFMArticle)
    }

    private fun convertLastFMArticleToCard(lastFMArticle: LastFMArticle): Card {
        return Card(lastFMArticle.artistName, lastFMArticle.biography, lastFMArticle.articleUrl, CardSource.LASTFM, lastFMArticle.lastFMlogoUrl)
    }

    private fun initLastFMService(): LastFMService {
        LastFMInjector.init()
        return LastFMInjector.lastFMService
    }
}