package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MoreDetailsPresenterImplTest(){
    private val repository: OtherInfoRepository = mockk()
    private val articleBiographyHelper: ArticleBiographyHelper = mockk()
    private val moreDetailsPresenter: MoreDetailsPresenter = MoreDetailsPresenterImpl(repository, articleBiographyHelper)

    @Test
    fun `notify open article (with url) should notify the result`(){
        val card = Card("artistName", "biography", "infoUrl")
        val expectedUIState = CardUIState("artistName", "biography", "infoUrl", true)

        every { repository.getCard("artistName") } returns card
        every { articleBiographyHelper.getDescription(card) } returns "biography"

        val stateTester: (CardUIState) -> Unit = mockk(relaxed = true)

        moreDetailsPresenter.cardObservable.subscribe { stateTester(it) }

        moreDetailsPresenter.updateCard("artistName")

        verify{stateTester(expectedUIState)}
    }

    @Test
    fun `notify open article (without url) should notify the result`(){
        val card = Card("artistName", "biography", "")
        val expectedUIState = CardUIState("artistName", "biography", "", false)

        every { repository.getCard("artistName") } returns card
        every { articleBiographyHelper.getDescription(card) } returns "biography"

        val stateTester: (CardUIState) -> Unit = mockk(relaxed = true)

        moreDetailsPresenter.cardObservable.subscribe { stateTester(it) }

        moreDetailsPresenter.updateCard("artistName")

        verify{stateTester(expectedUIState)}
    }
}