package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.ArtistArticleRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MoreDetailsPresenterImplTest(){
    private val repository: ArtistArticleRepository = mockk()
    private val articleBiographyHelper: ArticleBiographyHelper = mockk()
    private val moreDetailsPresenter: MoreDetailsPresenter = MoreDetailsPresenterImpl(repository, articleBiographyHelper)

    @Test
    fun `notify open article (with url) should notify the result`(){
        val card = Card("artistName", "biography", "infoUrl")
        val expectedUIState = MoreDetailsUIState("artistName", "biography", "infoUrl", true)

        every { repository.getArticleByArtistName("artistName") } returns card
        every { articleBiographyHelper.getDescription(card) } returns "biography"

        val stateTester: (MoreDetailsUIState) -> Unit = mockk(relaxed = true)

        moreDetailsPresenter.articleObservable.subscribe { stateTester(it) }

        moreDetailsPresenter.notifyOpenArticle("artistName")

        verify{stateTester(expectedUIState)}
    }

    @Test
    fun `notify open article (without url) should notify the result`(){
        val card = Card("artistName", "biography", "")
        val expectedUIState = MoreDetailsUIState("artistName", "biography", "", false)

        every { repository.getArticleByArtistName("artistName") } returns card
        every { articleBiographyHelper.getDescription(card) } returns "biography"

        val stateTester: (MoreDetailsUIState) -> Unit = mockk(relaxed = true)

        moreDetailsPresenter.articleObservable.subscribe { stateTester(it) }

        moreDetailsPresenter.notifyOpenArticle("artistName")

        verify{stateTester(expectedUIState)}
    }
}