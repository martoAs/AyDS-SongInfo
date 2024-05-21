package ayds.songinfo.moredetails.data


import ayds.artist.external.lastfm.data.ArticleTrackService
import ayds.artist.external.lastfm.data.LastFMArticle
import ayds.songinfo.moredetails.data.local.ArticleLocalStorage
import ayds.songinfo.moredetails.domain.Article
import ayds.songinfo.moredetails.domain.ArtistArticleRepository

internal class ArtistArticleRepositoryImp(private val articleLocalStorage: ArticleLocalStorage,
                                          private val articleTrackService: ArticleTrackService
) : ArtistArticleRepository {
    override fun getArticleByArtistName(artistName: String): Article {

        var artistArticle = articleLocalStorage.getArticleByArtistName(artistName)

        if (artistArticle != null){
            artistArticle = artistArticle.apply { markItAsLocal() }
        } else {

            val lastFMArticle = articleTrackService.getArticle(artistName)
            artistArticle = convertLastFMArticleToArticle(lastFMArticle)

            if (artistArticle.biography.isNotEmpty()) {
                articleLocalStorage.insertArticle(artistArticle)
            }
        }
        return artistArticle
    }

    private fun convertLastFMArticleToArticle(lastFMArticle: LastFMArticle): Article {
        return Article(lastFMArticle.artistName, lastFMArticle.biography, lastFMArticle.articleUrl)
    }
    private fun Article.markItAsLocal() {
        isLocallyStored = true
    }

}