package ayds.artist.external.lastfm.data
import java.io.IOException

interface LastFMService {
    fun getArticle(artistName: String): LastFMArticle
}

internal class LastFMServiceImpl(
    private val lastFMAPI: LastFMAPI,
    private val lastfmToArticleResolver: LastfmToArticleResolver
    ): LastFMService {
    override fun getArticle(artistName: String): LastFMArticle {
        var article = LastFMArticle(artistName, "", "")
        try{
            val callResponse = getArticleFromService(artistName)
            article = lastfmToArticleResolver.getArticleFromExternalData(callResponse.body(), artistName)
        }
        catch(e:IOException){
            e.printStackTrace()
        }
        return article
    }

    private fun getArticleFromService(artistName: String) = lastFMAPI.getArtistInfo(artistName).execute()
}