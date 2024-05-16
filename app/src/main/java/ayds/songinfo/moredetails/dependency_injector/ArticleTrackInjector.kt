package ayds.songinfo.moredetails.dependency_injector

import ayds.songinfo.moredetails.data.external.LastFMAPI
import ayds.songinfo.moredetails.data.external.ArticleTrackService
import ayds.songinfo.moredetails.data.external.ArticleTrackServiceImpl
import ayds.songinfo.moredetails.data.external.LastfmToArticleResolver
import ayds.songinfo.moredetails.data.external.LastfmToArticleResolverImpl
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