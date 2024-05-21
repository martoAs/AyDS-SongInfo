package ayds.artist.external.lastfm.data
import com.google.gson.Gson
import com.google.gson.JsonObject

interface LastfmToArticleResolver {
    fun getArticleFromExternalData(serviceData: String?, artistName: String): LastFMArticle
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
    ): LastFMArticle {
        val gson = Gson()

        val jasonObject = gson.fromJson(serviceData, JsonObject::class.java)

        val artist = jasonObject[ARTIST].getAsJsonObject()
        val bio = artist[BIO].getAsJsonObject()
        val extract = bio[CONTENT]
        val url = artist[URL]
        val text = extract?.asString ?: NO_RESULTS

        return LastFMArticle(artistName, text, url.asString)
    }
}