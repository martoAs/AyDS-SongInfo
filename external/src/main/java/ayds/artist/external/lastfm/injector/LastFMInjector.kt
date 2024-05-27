package ayds.artist.external.lastfm.injector

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import ayds.artist.external.lastfm.data.LastFMService
import ayds.artist.external.lastfm.data.LastFMServiceImpl
import ayds.artist.external.lastfm.data.LastFMAPI
import ayds.artist.external.lastfm.data.LastfmToArticleResolver

object LastFMInjector {
    private const val AUDIOSCROBBLER_PATH = "https://ws.audioscrobbler.com/2.0/"
    private lateinit var retrofit: Retrofit
    private lateinit var lastFMAPI: LastFMAPI
    private lateinit var lastfmToArticleResolver: LastfmToArticleResolver
    lateinit var lastFMService: LastFMService
    fun init() {
        retrofit = Retrofit.Builder()
            .baseUrl(AUDIOSCROBBLER_PATH)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        lastFMAPI = retrofit.create(LastFMAPI::class.java)
        lastfmToArticleResolver = ayds.artist.external.lastfm.data.LastfmToArticleResolverImpl()

        lastFMService = LastFMServiceImpl(
            lastFMAPI,
            lastfmToArticleResolver
        )
    }
}