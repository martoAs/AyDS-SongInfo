package ayds.songinfo.moredetails.injector

import android.content.Context
import androidx.room.Room
import ayds.songinfo.moredetails.data.MoreDetailsRepositoryImp
import ayds.songinfo.moredetails.data.external.Broker
import ayds.songinfo.moredetails.data.external.BrokerImpl
import ayds.songinfo.moredetails.data.local.CardDatabase
import ayds.songinfo.moredetails.data.local.MoreDetailsLocalStorage
import ayds.songinfo.moredetails.data.local.MoreDetailsLocalStorageImpl
import ayds.songinfo.moredetails.domain.MoreDetailsRepository
import ayds.songinfo.moredetails.presentation.MoreDetailsView

object MoreDetailsRepositoryInjector {

    private const val ARTICLE_BDNAME = "database-article"
    private lateinit var cardDatabase: CardDatabase
    private lateinit var localStorage: MoreDetailsLocalStorage
    lateinit var articleRepository : MoreDetailsRepository
    private lateinit var broker: Broker

    fun initMoreDetailsRepository(moreDetailsView: MoreDetailsView){
        cardDatabase = Room.databaseBuilder(
            moreDetailsView as Context,
            CardDatabase::class.java,
            ARTICLE_BDNAME
        ).build()

        localStorage = MoreDetailsLocalStorageImpl(cardDatabase)
        broker = BrokerImpl()
        articleRepository = MoreDetailsRepositoryImp(localStorage, broker)
    }
}