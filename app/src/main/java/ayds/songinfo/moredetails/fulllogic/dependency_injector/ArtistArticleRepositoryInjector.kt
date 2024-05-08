package ayds.songinfo.moredetails.fulllogic.dependency_injector

import android.content.Context
import androidx.room.Room
import ayds.songinfo.moredetails.fulllogic.data.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.data.ArticleLocalStorage
import ayds.songinfo.moredetails.fulllogic.data.ArticleLocalStorageImpl
import ayds.songinfo.moredetails.fulllogic.data.ArticleTrackService
import ayds.songinfo.moredetails.fulllogic.data.ArtistArticleRepositoryImpl
import ayds.songinfo.moredetails.fulllogic.domain.ArtistArticleRepository
import ayds.songinfo.moredetails.fulllogic.presentation.MoreDetailsView

object ArtistArticleRepositoryInjector {

    private const val ARTICLE_BDNAME = "database-article"
    private lateinit var articleDatabase: ArticleDatabase
    private lateinit var localStorage: ArticleLocalStorage
    private lateinit var trackService: ArticleTrackService
    lateinit var articleRepository : ArtistArticleRepository

    fun initArtistArticleRepository(moreDetailsView: MoreDetailsView){
        articleDatabase = Room.databaseBuilder(
            moreDetailsView as Context,
            ArticleDatabase::class.java,
            ARTICLE_BDNAME
        ).build()

        localStorage = ArticleLocalStorageImpl(articleDatabase)

        ArticleTrackInjector.init()
        trackService = ArticleTrackInjector.articleTrackService
        articleRepository = ArtistArticleRepositoryImpl(localStorage, trackService)
    }
}