package ayds.songinfo.moredetails.fulllogic.domain

import ayds.songinfo.moredetails.fulllogic.data.ArticleLocalStorage
import ayds.songinfo.moredetails.fulllogic.data.ArticleTrackService
import ayds.songinfo.moredetails.fulllogic.domain.Article.ArtistArticle
import ayds.songinfo.moredetails.fulllogic.domain.Article.EmptyArticle

interface ArtistArticleRepository {
    fun getArticleByArtistName(artistName:String?): Article
}

internal class ArtistArticleRepositoryImpl(private val articleLocalStorage: ArticleLocalStorage,
                                           private val articleTrackService: ArticleTrackService
) : ArtistArticleRepository{
    override fun getArticleByArtistName(artistName: String?):Article{
        artistName?.let {

            var artistArticle = articleLocalStorage.getArticleByArtistName(artistName)

            if (artistArticle != null){
                artistArticle = artistArticle.markItAsLocal()
            } else {
                artistArticle = articleTrackService.getArticle(artistName)

                artistArticle?.let{
                    artistArticle.biography.isNotEmpty().let { articleLocalStorage.insertArticle(artistArticle) }
                }
            }
            return artistArticle ?: EmptyArticle

        }?:let{ return EmptyArticle }
    }

    private fun ArtistArticle.markItAsLocal() = copy(biography = "[*]$biography")

}