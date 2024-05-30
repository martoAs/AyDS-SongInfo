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

            if (card.description.isNotEmpty()) {
                otherInfoLocalStorage.insertCard(card)
            }
        }
        return card
    }


    private fun Card.markItAsLocal() {
        isLocallyStored = true
    }

}