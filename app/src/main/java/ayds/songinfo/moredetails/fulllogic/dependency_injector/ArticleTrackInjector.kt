package ayds.songinfo.moredetails.fulllogic.dependency_injector

import ayds.songinfo.moredetails.fulllogic.data.LastFMAPI
import ayds.songinfo.moredetails.fulllogic.data.ArticleTrackService
import ayds.songinfo.moredetails.fulllogic.data.ArticleTrackServiceImpl
import ayds.songinfo.moredetails.fulllogic.data.LastfmToArticleResolver
import ayds.songinfo.moredetails.fulllogic.data.LastfmToArticleResolverImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object ArticleTrackInjector {

        private const val AUDIOSCROBBLER_PATH = "https://ws.audioscrobbler.com/2.0/"
        private lateinit var retrofit: Retrofit
        private lateinit var lastFMAPI: LastFMAPI
        private lateinit var lastfmToArticleResolver: LastfmToArticleResolver
        lateinit var articleTrackService: ArticleTrackService
        fun init() {
            retrofit = Retrofit.Builder()
                .baseUrl(AUDIOSCROBBLER_PATH)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()

            lastFMAPI = retrofit.create(LastFMAPI::class.java)
            lastfmToArticleResolver = LastfmToArticleResolverImpl()

            articleTrackService = ArticleTrackServiceImpl(lastFMAPI, lastfmToArticleResolver)
        }

}