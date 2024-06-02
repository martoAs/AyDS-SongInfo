package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.Broker
import ayds.songinfo.moredetails.data.local.MoreDetailsLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.MoreDetailsRepository

internal class MoreDetailsRepositoryImp(private val moreDetailsLocalStorage: MoreDetailsLocalStorage,
                                        private val broker: Broker) : MoreDetailsRepository {
    override fun getListOfCards(artistName: String): List<Card> {

        var cards = moreDetailsLocalStorage.getCards(artistName)

        if(cards.isEmpty()){
            cards = broker.getListOfCards(artistName)
            for (c in cards){
                if(c.description.isNotEmpty()){
                    moreDetailsLocalStorage.insertCard(c)
                }
            }
        }
        else
        {
            for (c in cards){
                c.markItAsLocal()
            }
        }

        return cards
    }


    private fun Card.markItAsLocal() {
        isLocallyStored = true
    }

}