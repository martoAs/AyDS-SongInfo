package ayds.songinfo.moredetails.data.external
import ayds.songinfo.moredetails.domain.Article
import com.google.gson.Gson
import com.google.gson.JsonObject

interface LastfmToArticleResolver {
    fun getArticleFromExternalData(serviceData: String?, artistName: String): Article
}

private const val URL = "url"
private const val CONTENT = "content"
private const val BIO = "bio"
private const val ARTIST = "artist"
private const val NO_RESULTS = "No Results"

internal class LastfmToArticleResolverImpl: LastfmToArticleResolver {
    override fun getArticleFromExternalData(
        serviceData: String?,
        artistName: String
    ): Article {
        val gson = Gson()

        val jasonObject = gson.fromJson(serviceData, JsonObject::class.java)

        val artist = jasonObject[ARTIST].getAsJsonObject()
        val bio = artist[BIO].getAsJsonObject()
        val extract = bio[CONTENT]
        val url = artist[URL]
        val text = extract?.asString ?: NO_RESULTS

        return Article(artistName, text, url.asString)
    }
}