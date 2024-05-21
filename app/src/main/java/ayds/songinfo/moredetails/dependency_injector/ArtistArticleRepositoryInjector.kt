package ayds.songinfo.moredetails.dependency_injector

import android.content.Context
import androidx.room.Room
import ayds.songinfo.moredetails.data.local.ArticleDatabase
import ayds.songinfo.moredetails.data.local.ArticleLocalStorage
import ayds.songinfo.moredetails.data.local.ArticleLocalStorageImpl
import ayds.songinfo.moredetails.domain.ArtistArticleRepository
import ayds.songinfo.moredetails.data.ArtistArticleRepositoryImp
import ayds.songinfo.moredetails.presentation.MoreDetailsView
import ayds.artist.external.lastfm.injector.LastFMInjector
object ArtistArticleRepositoryInjector {

    private const val ARTICLE_BDNAME = "database-article"
    private lateinit var articleDatabase: ArticleDatabase
    private lateinit var localStorage: ArticleLocalStorage
    private lateinit var trackService: ayds.artist.external.lastfm.data.ArticleTrackService
    lateinit var articleRepository : ArtistArticleRepository

    fun initArtistArticleRepository(moreDetailsView: MoreDetailsView){
        articleDatabase = Room.databaseBuilder(
            moreDetailsView as Context,
            ArticleDatabase::class.java,
            ARTICLE_BDNAME
        ).build()

        localStorage = ArticleLocalStorageImpl(articleDatabase)

        LastFMInjector.init()
        trackService = LastFMInjector.articleTrackService
        articleRepository = ArtistArticleRepositoryImp(localStorage, trackService)
    }
}