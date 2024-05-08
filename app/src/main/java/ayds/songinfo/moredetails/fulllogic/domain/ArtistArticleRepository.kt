package ayds.songinfo.moredetails.fulllogic.domain

interface ArtistArticleRepository {
    fun getArticleByArtistName(artistName:String?): Article
}

