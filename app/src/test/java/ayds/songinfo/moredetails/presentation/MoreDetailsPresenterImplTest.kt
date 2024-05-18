package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Article
import ayds.songinfo.moredetails.domain.ArtistArticleRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import io.mockk.verify
import org.junit.Assert.assertEquals

class MoreDetailsPresenterImplTest(){
    private val repository: ArtistArticleRepository = mockk()
    private val articleBiographyHelper: ArticleBiographyHelper = mockk()
    private val moreDetailsPresenter: MoreDetailsPresenter = MoreDetailsPresenterImpl(repository, articleBiographyHelper)

    @Test
    fun notifyOpenArticleTest(){
        val article = Article("artistName", "biography", "infoUrl")
        val expectedUIState = MoreDetailsUIState("artistName", "biography", "infoUrl", true)

        every { repository.getArticleByArtistName("artistName") } returns article
        every { articleBiographyHelper.getDescription(article) } returns "biography"

        val stateTester: (MoreDetailsUIState) -> Unit = mockk(relaxed = true)

        moreDetailsPresenter.articleObservable.subscribe { stateTester(it) }

        moreDetailsPresenter.notifyOpenArticle("artistName")

        verify{stateTester(expectedUIState)}


    }
}