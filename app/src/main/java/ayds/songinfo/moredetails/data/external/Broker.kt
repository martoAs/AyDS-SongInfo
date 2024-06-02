package ayds.songinfo.moredetails.data.external

import ayds.songinfo.moredetails.domain.Card

interface Broker {
    fun getListOfCards(artistName: String): MutableList<Card>
}

internal class BrokerImpl() : Broker {

    private val proxyList: List<ProxyInterface> = getProxys()
    override fun getListOfCards(artistName: String): MutableList<Card> {
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

