package ayds.songinfo.moredetails.dependency_injector

import ayds.songinfo.moredetails.presentation.ArticleBiographyHelperImpl
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenter
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenterImpl
import ayds.songinfo.moredetails.presentation.MoreDetailsView

object MoreDetailsInjector {
    private lateinit var moreDetailsPresenter : MoreDetailsPresenter

    fun getMoreDetailsPresenter(): MoreDetailsPresenter = moreDetailsPresenter
    fun init(moreDetailsView: MoreDetailsView){
        ArtistArticleRepositoryInjector.initArtistArticleRepository(moreDetailsView)
        val artistBiographyHelper = ArticleBiographyHelperImpl()
        moreDetailsPresenter = MoreDetailsPresenterImpl(ArtistArticleRepositoryInjector.articleRepository, artistBiographyHelper)
    }
}