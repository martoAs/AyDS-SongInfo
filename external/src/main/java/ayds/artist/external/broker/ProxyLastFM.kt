package ayds.artist.external.broker

import ayds.artist.external.lastfm.data.LastFMArticle
import ayds.artist.external.lastfm.data.LastFMService
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.CardSource

internal class ProxyLastFMImpl(private val lastFMService: LastFMService):ProxyInterface{
    override fun get(artistName: String):Card{
        val lastFMArticle = lastFMService.getArticle(artistName)
        return convertLastFMArticleToCard(lastFMArticle)
    }

    private fun convertLastFMArticleToCard(lastFMArticle: LastFMArticle): Card {
        return Card(lastFMArticle.artistName, lastFMArticle.biography, lastFMArticle.articleUrl, CardSource.LASTFM, lastFMArticle.lastFMlogoUrl)
    }
}