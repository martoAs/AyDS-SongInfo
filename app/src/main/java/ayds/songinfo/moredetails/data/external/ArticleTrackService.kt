package ayds.songinfo.moredetails.data.external
import ayds.songinfo.moredetails.domain.Article
import java.io.IOException

interface ArticleTrackService {
    fun getArticle(artistName: String): Article
}

internal class ArticleTrackServiceImpl(
    private val lastFMAPI: LastFMAPI,
    private val lastfmToArticleResolver: LastfmToArticleResolver
    ): ArticleTrackService {
    override fun getArticle(artistName: String): Article {
        var article = Article(artistName, "", "")
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