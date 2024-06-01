package ayds.songinfo.moredetails.domain

interface OtherInfoRepository {
    fun getListOfCards(artistName:String): List<Card>
}

