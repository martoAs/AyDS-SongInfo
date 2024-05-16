package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.ArticleTrackService
import ayds.songinfo.moredetails.data.local.ArticleLocalStorage
import ayds.songinfo.moredetails.domain.Article
import ayds.songinfo.moredetails.domain.ArtistArticleRepository

internal class ArtistArticleRepositoryImp(private val articleLocalStorage: ArticleLocalStorage,
                                          private val articleTrackService: ArticleTrackService
) : ArtistArticleRepository {
    override fun getArticleByArtistName(artistName: String?): Article {
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
            return artistArticle ?: Article.EmptyArticle

        }?:let{ return Article.EmptyArticle }
    }

    private fun Article.ArtistArticle.markItAsLocal() = copy(biography = "[*]$biography")


}