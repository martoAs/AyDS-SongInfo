package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.ArticleTrackService
import ayds.songinfo.moredetails.data.local.ArticleLocalStorage
import ayds.songinfo.moredetails.domain.Article
import ayds.songinfo.moredetails.domain.ArtistArticleRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test


class ArtistArticleRepositoryImpTest {
    private val articleLocalStorage: ArticleLocalStorage = mockk(relaxUnitFun = true)
    private val articleTrackService: ArticleTrackService = mockk(relaxUnitFun = true)
    private val artistArticleRepository: ArtistArticleRepository =
        ArtistArticleRepositoryImp(articleLocalStorage, articleTrackService)

    @Test
    fun `given local article should return it marked as local`() {
        val artist = "artistName"
        val article = Article(artist, "biography", "infoUrl")
        every { articleLocalStorage.getArticleByArtistName(artist) } returns article

        val resultArticle = artistArticleRepository.getArticleByArtistName(artist)
        val changedBiography = "[*]biography"

        assertEquals(article.artistName, resultArticle.artistName)
        assertEquals(changedBiography, resultArticle.biography)
        assertEquals(article.articleUrl, resultArticle.articleUrl)
    }

    @Test
    fun `given non local article should return article (with biography) from external service and store it locally`() {
        val artist = "artistName"
        val article = Article(artist, "biography", "infoUrl")
        every { articleLocalStorage.getArticleByArtistName(artist) } returns null
        every { articleTrackService.getArticle(artist) } returns article

        val resultArticle = artistArticleRepository.getArticleByArtistName(artist)

        assertEquals(article, resultArticle)
        verify { articleLocalStorage.insertArticle(article) }
    }

    @Test
    fun `given non local article should return article (without biography) from external service`() {
        val artist = "artistName"
        val article = Article(artist, "", "infoUrl")
        every { articleLocalStorage.getArticleByArtistName(artist) } returns null
        every { articleTrackService.getArticle(artist) } returns article

        val resultArticle = artistArticleRepository.getArticleByArtistName(artist)

        assertEquals(article, resultArticle)
        verify(exactly = 0){ articleLocalStorage.insertArticle(article) }
    }
}