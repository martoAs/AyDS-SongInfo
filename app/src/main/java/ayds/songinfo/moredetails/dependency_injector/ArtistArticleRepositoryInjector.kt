package ayds.songinfo.moredetails.dependency_injector

import android.content.Context
import androidx.room.Room
import ayds.songinfo.moredetails.data.local.CardDatabase
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorageImpl
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import ayds.songinfo.moredetails.data.OtherInfoRepositoryImp
import ayds.songinfo.moredetails.presentation.MoreDetailsView
import ayds.artist.external.lastfm.injector.LastFMInjector
object ArtistArticleRepositoryInjector {

    private const val ARTICLE_BDNAME = "database-article"
    private lateinit var cardDatabase: CardDatabase
    private lateinit var localStorage: OtherInfoLocalStorage
    private lateinit var trackService: ayds.artist.external.lastfm.data.LastFMService
    lateinit var articleRepository : OtherInfoRepository

    fun initArtistArticleRepository(moreDetailsView: MoreDetailsView){
        cardDatabase = Room.databaseBuilder(
            moreDetailsView as Context,
            CardDatabase::class.java,
            ARTICLE_BDNAME
        ).build()

        localStorage = OtherInfoLocalStorageImpl(cardDatabase)

        LastFMInjector.init()
        trackService = LastFMInjector.lastFMService
        articleRepository = OtherInfoRepositoryImp(localStorage, trackService)
    }
}