package ayds.songinfo.moredetails.data

import org.junit.jupiter.api.Assertions.*
import ayds.songinfo.moredetails.data.external.ArticleTrackService
import ayds.songinfo.moredetails.data.local.ArticleLocalStorage
import ayds.songinfo.moredetails.domain.Article
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test


class ArtistArticleRepositoryImpTest {
    private val articleLocalStorage: ArticleLocalStorage = mockk(relaxUnitFun = true)
    private val articleTrackService: ArticleTrackService = mockk(relaxUnitFun = true)
    private val artistArticleRepository =
        ArtistArticleRepositoryImp(articleLocalStorage, articleTrackService)

    @Test
    fun articleIsInLocalStorage() {
        val artist = "artistName"
        val article = Article(artist, "biography", "infoUrl")
        every { articleLocalStorage.getArticleByArtistName(artist) } returns article

        val resultArticle = artistArticleRepository.getArticleByArtistName(artist)
        val changedBiography = "[*]biography"

        assertEquals(changedBiography, resultArticle.biography)
    }

    @Test
    fun articleIsNotInLocalStorageAndBiographyNotEmpty() {
        val artist = "artistName"
        val article = Article(artist, "biography", "infoUrl")
        every { articleLocalStorage.getArticleByArtistName(artist) } returns null
        every { articleTrackService.getArticle(artist) } returns article

        val resultArticle = artistArticleRepository.getArticleByArtistName(artist)

        assertEquals(article, resultArticle)


    }
}