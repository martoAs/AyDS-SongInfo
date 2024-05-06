package ayds.songinfo.moredetails.fulllogic.data
import ayds.songinfo.moredetails.fulllogic.domain.Article.ArtistArticle
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.util.Locale

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
        val bio = getBiographyFromJSON(artist, artistName)
        val url = getUrl(artist)

        return ArtistArticle(artistName, bio, url.asString)
    }

    private fun getUrl(artist: JsonObject): JsonElement = artist[URL]
    private fun getExtract(bio: JsonObject): JsonElement? = bio[CONTENT]
    private fun getBio(artist: JsonObject): JsonObject = artist[BIO].getAsJsonObject()
    private fun getArtist(jobj: JsonObject): JsonObject = jobj[ARTIST].getAsJsonObject()
    private fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
            )
        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
    }
    private fun getBiographyFromJSON(
        artist: JsonObject,
        artistName: String
    ): String {
        val bio = getBio(artist)
        val extract = getExtract(bio)

        return extract?.let { getFormattedText(it, artistName) } ?: NO_RESULTS
    }

    private fun getFormattedText(
        extract: JsonElement,
        artistName: String
    ): String {
        var text = extract.asString.replace("\\n", "\n")
        text = textToHtml(text, artistName)
        return text
    }




}