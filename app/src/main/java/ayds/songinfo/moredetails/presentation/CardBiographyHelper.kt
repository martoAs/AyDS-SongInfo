package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Card
import java.util.Locale

interface ArticleBiographyHelper {
    fun getDescription(card: Card): String
}

private const val HEADER = "<html><div width=400><font face=\"arial\">"
private const val FOOTER = "</font></div></html>"

internal class ArticleBiographyHelperImpl : ArticleBiographyHelper {

    override fun getDescription(card: Card): String {
        val text = getTextBiography(card)
        return textToHtml(text, card.artistName)
    }

    private fun getTextBiography(artistBiography: Card): String {
        val prefix = if (artistBiography.isLocallyStored) "[*]" else ""
        val text = artistBiography.description.replace("\\n", "\n")
        return "$prefix$text"
    }


    private fun textToHtml(text: String, term: String): String {
        val builder = StringBuilder()
        builder.append(HEADER)
        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term.uppercase(Locale.getDefault()) + "</b>"
            )
        builder.append(textWithBold)
        builder.append(FOOTER)
        return builder.toString()
    }
}