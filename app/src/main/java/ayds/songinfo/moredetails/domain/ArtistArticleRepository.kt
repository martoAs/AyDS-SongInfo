package ayds.songinfo.moredetails.domain

interface ArtistArticleRepository {
    fun getArticleByArtistName(artistName:String?): Article
}

