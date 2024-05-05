package ayds.songinfo.moredetails.fulllogic.dependency_injector

import ayds.songinfo.moredetails.fulllogic.presentation.MoreDetailsPresenter
import ayds.songinfo.moredetails.fulllogic.presentation.MoreDetailsPresenterImpl
import ayds.songinfo.moredetails.fulllogic.presentation.MoreDetailsView

object MoreDetailsInjector {
    private val repositoryInjector = ArtistArticleRepositoryInjector
    private lateinit var moreDetailsPresenter : MoreDetailsPresenter

    fun getMoreDetailsPresenter(): MoreDetailsPresenter = moreDetailsPresenter
    fun init(moreDetailsView: MoreDetailsView){
        repositoryInjector.initArtistArticleRepository(moreDetailsView)
        moreDetailsPresenter = MoreDetailsPresenterImpl(repositoryInjector.articleRepository)
    }
}