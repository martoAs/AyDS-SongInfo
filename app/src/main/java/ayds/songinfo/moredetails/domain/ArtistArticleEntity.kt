package ayds.songinfo.moredetails.domain

sealed class Article{
    data class ArtistArticle(
        val artistName: String,
        val biography: String,
        val articleUrl: String) : Article()
    object EmptyArticle: Article()
}