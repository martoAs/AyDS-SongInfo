package ayds.songinfo.moredetails.fulllogic.data
import ayds.songinfo.moredetails.fulllogic.domain.Article.ArtistArticle
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject

interface LastfmToArticleResolver {
    fun getArticleFromExternalData(serviceData: String?, artistName: String): ArtistArticle?
}

private const val URL = "url"
private const val CONTENT = "content"
private const val BIO = "bio"
private const val ARTIST = "artist"
private const val NO_RESULTS = "No Results"

internal class LastfmToArticleResolverImpl:LastfmToArticleResolver{
    override fun getArticleFromExternalData(
        serviceData: String?,
        artistName: String
    ): ArtistArticle {
        val gson = Gson()
        val jobj = gson.fromJson(serviceData, JsonObject::class.java)

        val artist = getArtist(jobj)
        val bio = getBio(artist)
        val extract = getExtract(bio)
        val url = getUrl(artist)
        val text = getText(extract)

        return ArtistArticle(artistName, text, url.asString)
    }

    private fun getText(extract: JsonElement?) = extract?.asString ?: NO_RESULTS
    private fun getUrl(artist: JsonObject): JsonElement = artist[URL]
    private fun getExtract(bio: JsonObject): JsonElement? = bio[CONTENT]
    private fun getBio(artist: JsonObject): JsonObject = artist[BIO].getAsJsonObject()
    private fun getArtist(jobj: JsonObject): JsonObject = jobj[ARTIST].getAsJsonObject()

}