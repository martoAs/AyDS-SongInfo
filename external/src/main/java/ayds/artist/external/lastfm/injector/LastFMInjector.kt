package ayds.artist.external.lastfm.injector

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import ayds.artist.external.lastfm.data.ArticleTrackService
import ayds.artist.external.lastfm.data.ArticleTrackServiceImpl
import ayds.artist.external.lastfm.data.LastFMAPI
import ayds.artist.external.lastfm.data.LastfmToArticleResolver

object LastFMInjector {
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
        lastfmToArticleResolver = ayds.artist.external.lastfm.data.LastfmToArticleResolverImpl()

        articleTrackService = ArticleTrackServiceImpl(
            lastFMAPI,
            lastfmToArticleResolver
        )
    }
}