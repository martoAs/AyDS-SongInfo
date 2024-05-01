package ayds.songinfo.moredetails.fulllogic.domain

import ayds.songinfo.moredetails.fulllogic.data.ArticleLocalStorage
import ayds.songinfo.moredetails.fulllogic.data.ArticleTrackService
import ayds.songinfo.moredetails.fulllogic.domain.Article.ArtistArticle
import ayds.songinfo.moredetails.fulllogic.domain.Article.EmptyArticle
import java.io.IOException

interface ArtistArticleRepository {
    fun getArticleByArtistName(artistName:String?): Article
}

internal class ArtistArticleRepositoryImpl(private val articleLocalStorage: ArticleLocalStorage,
                                           private val articleTrackService: ArticleTrackService
) : ArtistArticleRepository{
    override fun getArticleByArtistName(artistName: String?):Article{
        artistName?.let {
            val databaseArticle = articleLocalStorage.getArticleByArtistName(artistName)

            val artistArticle: ArtistArticle

            if (databaseArticle != null) {
                artistArticle = databaseArticle.markItAsLocal()
            } else {
                artistArticle = getArticleFromService(artistName)
                if (artistArticle.biography.isNotEmpty()) {
                    saveInformationToBD(artistArticle)
                }
            }
            return artistArticle
        }?:let{ return EmptyArticle}
    }

    private fun ArtistArticle.markItAsLocal() = copy(biography = "[*]$biography")

    private fun getArticleFromService(artistName: String): ArtistArticle {

        var artistArticle = ArtistArticle(artistName, "", "")
        try {
           articleTrackService.getArticle(artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return artistArticle
    }

    private fun saveInformationToBD(artistArticle: ArtistArticle) {
        articleLocalStorage.insertArticle(artistArticle)
    }

}