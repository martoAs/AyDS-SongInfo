package ayds.songinfo.moredetails.domain

interface MoreDetailsRepository {
    fun getListOfCards(artistName:String): List<Card>
}

