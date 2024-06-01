package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.OtherInfoRepository

internal class OtherInfoRepositoryImp(private val otherInfoLocalStorage: OtherInfoLocalStorage
) : OtherInfoRepository {
    override fun getListOfCards(artistName: String): List<Card> {

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