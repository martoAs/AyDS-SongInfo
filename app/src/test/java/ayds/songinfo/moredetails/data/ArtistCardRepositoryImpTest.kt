package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.ArticleTrackService
import ayds.songinfo.moredetails.data.local.MoreDetailsLocalStorage
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.MoreDetailsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test


class ArtistCardRepositoryImpTest {
    private val moreDetailsLocalStorage: MoreDetailsLocalStorage = mockk(relaxUnitFun = true)
    private val articleTrackService: ayds.artist.external.lastfm.ArticleTrackService = mockk(relaxUnitFun = true)
    private val moreDetailsRepository: MoreDetailsRepository =
        MoreDetailsRepositoryImp(moreDetailsLocalStorage, articleTrackService)

    @Test
    fun `given local article should return it marked as local`() {
        val artist = "artistName"
        val card = Card(artist, "biography", "infoUrl")
        every { moreDetailsLocalStorage.getCards(artist) } returns card

        val resultArticle = moreDetailsRepository.getListOfCards(artist)
        val changedBiography = "[*]biography"

        assertEquals(card.artistName, resultArticle.artistName)
        assertEquals(changedBiography, resultArticle.description)
        assertEquals(card.infoUrl, resultArticle.infoUrl)
    }

    @Test
    fun `given non local article should return article (with biography) from external service and store it locally`() {
        val artist = "artistName"
        val card = Card(artist, "biography", "infoUrl")
        every { moreDetailsLocalStorage.getCards(artist) } returns null
        every { articleTrackService.getArticle(artist) } returns card

        val resultArticle = moreDetailsRepository.getListOfCards(artist)

        assertEquals(card, resultArticle)
        verify { moreDetailsLocalStorage.insertCard(card) }
    }

    @Test
    fun `given non local article should return article (without biography) from external service`() {
        val artist = "artistName"
        val card = Card(artist, "", "infoUrl")
        every { moreDetailsLocalStorage.getCards(artist) } returns null
        every { articleTrackService.getArticle(artist) } returns card

        val resultArticle = moreDetailsRepository.getListOfCards(artist)

        assertEquals(card, resultArticle)
        verify(exactly = 0){ moreDetailsLocalStorage.insertCard(card) }
    }
}