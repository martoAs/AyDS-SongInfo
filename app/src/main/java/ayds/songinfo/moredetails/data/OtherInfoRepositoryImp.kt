package ayds.songinfo.moredetails.data


import ayds.artist.external.lastfm.data.LastFMService
import ayds.artist.external.lastfm.data.LastFMArticle
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.OtherInfoRepository

internal class OtherInfoRepositoryImp(private val otherInfoLocalStorage: OtherInfoLocalStorage,
                                      private val lastFMService: LastFMService
) : OtherInfoRepository {
    override fun getCard(artistName: String): Card {

        var card = otherInfoLocalStorage.getCard(artistName)

        if (card != null){
            card = card.apply { markItAsLocal() }
        } else {

            val lastFMArticle = lastFMService.getArticle(artistName)
            card = convertLastFMArticleToCard(lastFMArticle)

            if (card.description.isNotEmpty()) {
                otherInfoLocalStorage.insertCard(card)
            }
        }
        return card
    }

    private fun convertLastFMArticleToCard(lastFMArticle: LastFMArticle): Card {
        return Card(lastFMArticle.artistName, lastFMArticle.biography, lastFMArticle.articleUrl)
    }
    private fun Card.markItAsLocal() {
        isLocallyStored = true
    }

}