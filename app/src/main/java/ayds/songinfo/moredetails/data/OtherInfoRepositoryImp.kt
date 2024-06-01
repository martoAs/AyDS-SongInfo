package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.Broker
import ayds.songinfo.moredetails.data.external.BrokerImpl
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.OtherInfoRepository

internal class OtherInfoRepositoryImp(private val otherInfoLocalStorage: OtherInfoLocalStorage
) : OtherInfoRepository {

    private val broker: Broker = BrokerImpl()
    override fun getListOfCards(artistName: String): List<Card> {

        var cards = otherInfoLocalStorage.getCards(artistName)
        if(cards.isEmpty()){
            cards = broker.getListOfCards(artistName)
            for (c in cards){
                if(c.description.isNotEmpty()){
                    otherInfoLocalStorage.insertCard(c)
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