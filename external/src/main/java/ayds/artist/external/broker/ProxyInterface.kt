package ayds.artist.external.broker

import ayds.songinfo.moredetails.domain.Card

interface ProxyInterface{
    fun get(artistName: String): Card
}