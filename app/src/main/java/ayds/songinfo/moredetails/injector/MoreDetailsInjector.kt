package ayds.songinfo.moredetails.injector

import ayds.songinfo.moredetails.presentation.CardBiographyHelperImpl
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenter
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenterImpl
import ayds.songinfo.moredetails.presentation.MoreDetailsView

object MoreDetailsInjector {
    private lateinit var moreDetailsPresenter : MoreDetailsPresenter

    fun getMoreDetailsPresenter(): MoreDetailsPresenter = moreDetailsPresenter
    fun init(moreDetailsView: MoreDetailsView){
        ArtistArticleRepositoryInjector.initArtistArticleRepository(moreDetailsView)
        val artistBiographyHelper = CardBiographyHelperImpl()
        moreDetailsPresenter = MoreDetailsPresenterImpl(ArtistArticleRepositoryInjector.articleRepository, artistBiographyHelper)
    }
}