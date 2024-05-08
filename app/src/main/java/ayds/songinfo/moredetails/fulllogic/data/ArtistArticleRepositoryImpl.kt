package ayds.songinfo.moredetails.fulllogic.data

import ayds.songinfo.moredetails.fulllogic.domain.Article
import ayds.songinfo.moredetails.fulllogic.domain.ArtistArticleRepository

class ArtistArticleRepositoryImpl(private val articleLocalStorage: ArticleLocalStorage,
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