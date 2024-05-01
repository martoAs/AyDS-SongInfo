package ayds.songinfo.moredetails.fulllogic.data
import ayds.songinfo.moredetails.fulllogic.LastFMAPI
import ayds.songinfo.moredetails.fulllogic.domain.Article.ArtistArticle

interface ArticleTrackService {
    fun getArticle(artistName: String): ArtistArticle?
}

internal class ArticleTrackServiceImpl(
    private val lastFMAPI: LastFMAPI,
    private val lastfmToArticleResolver: LastfmToArticleResolver
    ):ArticleTrackService{
    override fun getArticle(artistName: String): ArtistArticle? {
        val callResponse = getArticleFromService(artistName)
        return lastfmToArticleResolver.getArticleFromExternalData(callResponse.body(), artistName)
    }

    private fun getArticleFromService(artistName: String) = lastFMAPI.getArtistInfo(artistName).execute()
}