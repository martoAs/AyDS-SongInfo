package ayds.songinfo.moredetails.injector

import ayds.songinfo.moredetails.presentation.CardBiographyHelperImpl
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenter
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenterImpl
import ayds.songinfo.moredetails.presentation.MoreDetailsView

object MoreDetailsInjector {
    private lateinit var moreDetailsPresenter : MoreDetailsPresenter

    fun getMoreDetailsPresenter(): MoreDetailsPresenter = moreDetailsPresenter
    fun init(moreDetailsView: MoreDetailsView){
        MoreDetailsRepositoryInjector.initMoreDetailsRepository(moreDetailsView)
        val artistBiographyHelper = CardBiographyHelperImpl()
        moreDetailsPresenter = MoreDetailsPresenterImpl(MoreDetailsRepositoryInjector.articleRepository, artistBiographyHelper)
    }
}