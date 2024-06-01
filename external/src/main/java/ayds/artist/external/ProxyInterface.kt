package ayds.artist.external

import ayds.songinfo.moredetails.domain.Card

interface ProxyInterface{
    fun get(artistName: String): Card
}