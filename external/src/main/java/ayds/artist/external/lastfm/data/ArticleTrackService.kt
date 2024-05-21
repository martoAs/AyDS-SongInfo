package ayds.artist.external.lastfm.data
import ayds.artist.external.lastfm.data.LastFMAPI
import ayds.artist.external.lastfm.data.LastfmToArticleResolver
import ayds.artist.external.lastfm.data.LastFMArticle
import java.io.IOException

interface ArticleTrackService {
    fun getArticle(artistName: String): LastFMArticle
}

internal class ArticleTrackServiceImpl(
    private val lastFMAPI: LastFMAPI,
    private val lastfmToArticleResolver: LastfmToArticleResolver
    ): ArticleTrackService {
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