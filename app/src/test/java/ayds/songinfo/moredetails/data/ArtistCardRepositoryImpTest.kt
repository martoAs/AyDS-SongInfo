package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.ArticleTrackService
import ayds.songinfo.moredetails.data.local.ArticleLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.ArtistArticleRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test


class ArtistCardRepositoryImpTest {
    private val articleLocalStorage: ArticleLocalStorage = mockk(relaxUnitFun = true)
    private val articleTrackService: ayds.artist.external.lastfm.ArticleTrackService = mockk(relaxUnitFun = true)
    private val artistArticleRepository: ArtistArticleRepository =
        ArtistArticleRepositoryImp(articleLocalStorage, articleTrackService)

    @Test
    fun `given local article should return it marked as local`() {
        val artist = "artistName"
        val card = Card(artist, "biography", "infoUrl")
        every { articleLocalStorage.getArticleByArtistName(artist) } returns card

        val resultArticle = artistArticleRepository.getArticleByArtistName(artist)
        val changedBiography = "[*]biography"

        assertEquals(card.artistName, resultArticle.artistName)
        assertEquals(changedBiography, resultArticle.description)
        assertEquals(card.infoUrl, resultArticle.infoUrl)
    }

    @Test
    fun `given non local article should return article (with biography) from external service and store it locally`() {
        val artist = "artistName"
        val card = Card(artist, "biography", "infoUrl")
        every { articleLocalStorage.getArticleByArtistName(artist) } returns null
        every { articleTrackService.getArticle(artist) } returns card

        val resultArticle = artistArticleRepository.getArticleByArtistName(artist)

        assertEquals(card, resultArticle)
        verify { articleLocalStorage.insertArticle(card) }
    }

    @Test
    fun `given non local article should return article (without biography) from external service`() {
        val artist = "artistName"
        val card = Card(artist, "", "infoUrl")
        every { articleLocalStorage.getArticleByArtistName(artist) } returns null
        every { articleTrackService.getArticle(artist) } returns card

        val resultArticle = artistArticleRepository.getArticleByArtistName(artist)

        assertEquals(card, resultArticle)
        verify(exactly = 0){ articleLocalStorage.insertArticle(card) }
    }
}