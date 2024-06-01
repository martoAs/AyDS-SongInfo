package ayds.songinfo.moredetails.data.external

import ayds.artist.external.ProxyInterface
import ayds.artist.external.lastfm.data.ProxyLastFMImpl
import ayds.artist.external.newyorktimes.data.ProxyNYTImpl
import ayds.artist.external.wikipedia.data.ProxyWikipedia
import ayds.songinfo.moredetails.domain.Card

interface Broker {
    fun getLisOfCards(artistName: String): MutableList<Card>
}

internal class BrokerImpl() : Broker {

    private val proxyList: List<ProxyInterface> = getProxys()
    override fun getLisOfCards(artistName: String): MutableList<Card> {
        val cardList = mutableListOf<Card>()
        for (proxy in proxyList) {
            cardList.add(proxy.get(artistName))
        }
        return cardList
    }

    private fun getProxys(): List<ProxyInterface> {
        val proxyList = mutableListOf<ProxyInterface>()
        proxyList.add(ProxyLastFMImpl())
        proxyList.add(ProxyWikipedia())
        proxyList.add(ProxyNYTImpl())
        return proxyList
    }





}

