package ayds.songinfo.moredetails.data.external

import ayds.songinfo.moredetails.domain.Card
interface ProxyInterface{
    fun get(artistName: String): Card
}