package ayds.songinfo.moredetails.fulllogic.dependency_injector

import android.content.Context
import androidx.room.Room
import ayds.songinfo.moredetails.fulllogic.data.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.data.ArticleLocalStorage
import ayds.songinfo.moredetails.fulllogic.data.ArticleLocalStorageImpl
import ayds.songinfo.moredetails.fulllogic.data.ArticleTrackService
import ayds.songinfo.moredetails.fulllogic.domain.ArtistArticleRepository
import ayds.songinfo.moredetails.fulllogic.domain.ArtistArticleRepositoryImpl
import ayds.songinfo.moredetails.fulllogic.presentation.MoreDetailsView

object ArtistArticleRepositoryInjector {

    private const val ARTICLE_BDNAME = "database-article"
    private lateinit var articleDatabase: ArticleDatabase
    private val localStorage: ArticleLocalStorage = ArticleLocalStorageImpl(articleDatabase)
    private val trackService: ArticleTrackService = ArticleTrackInjector.articleTrackService
    val articleRepository : ArtistArticleRepository = ArtistArticleRepositoryImpl(localStorage, trackService)
    fun initArtistArticleRepository(moreDetailsView: MoreDetailsView){
        articleDatabase = Room.databaseBuilder(
            moreDetailsView as Context,
            ArticleDatabase::class.java,
            ARTICLE_BDNAME
        ).build()
    }
}