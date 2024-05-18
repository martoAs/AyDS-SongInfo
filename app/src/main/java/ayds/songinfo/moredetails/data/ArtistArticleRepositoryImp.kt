package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.ArticleTrackService
import ayds.songinfo.moredetails.data.local.ArticleLocalStorage
import ayds.songinfo.moredetails.domain.Article
import ayds.songinfo.moredetails.domain.ArtistArticleRepository

internal class ArtistArticleRepositoryImp(private val articleLocalStorage: ArticleLocalStorage,
                                          private val articleTrackService: ArticleTrackService
) : ArtistArticleRepository {
    override fun getArticleByArtistName(artistName: String): Article {

        var artistArticle = articleLocalStorage.getArticleByArtistName(artistName)

        if (artistArticle != null){
            artistArticle = artistArticle.markItAsLocal()
        } else {
            artistArticle = articleTrackService.getArticle(artistName)

            if (artistArticle.biography.isNotEmpty()) {
                articleLocalStorage.insertArticle(artistArticle)
            }
        }
        return artistArticle
    }

    private fun Article.markItAsLocal() = copy(biography = "[*]$biography")

}