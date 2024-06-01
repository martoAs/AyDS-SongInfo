package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Card
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Locale

class CardBiographyHelperImplTest {

    private val cardBiographyHelper: CardBiographyHelper = CardBiographyHelperImpl()
    private val footer = "</font></div></html>"
    private val header = "<html><div width=400><font face=\"arial\">"
    @Test
    fun getDescriptionTest(){
        val articleText = "Hello, this is a description of the Red Hot Chilli Peppers. They're the best.\n " +
                "Psychic spies from China try to steal your mind's elation."
        val card = Card("Red Hot Chilli Peppers", articleText, "infoUrl")
        val expectedDescription = "${header}Hello, this is a description of the <b>RED HOT CHILLI PEPPERS</b>. They re the best.<br> Psychic spies from China try to steal your mind s elation.${footer}"

        val resultDescription = cardBiographyHelper.getDescription(card)

        assertEquals(expectedDescription, resultDescription)
    }

    /* CASOS LÍMITE:
    *      - El artist name nunca es vacío por el flujo de ejecución, no se encontraría una canción por lo que el botón de more details estaría deshabilitado
    *      - La url del article no se utiliza */

    @Test
    fun `get description, article without biography`(){
        val card = Card("artist name", "", "infoUrl")
        val expectedDescription = "${header}${footer}"

        val resultDescription = cardBiographyHelper.getDescription(card)

        assertEquals(expectedDescription, resultDescription)
    }

    @Test
    fun `get description, biography con caracteres especiales`(){
        val articleText = "Hello, this is a description of the Red Hot Chilli Peppers. They're the best.\n " +
                "Psychic spies from China try to steal your mind's elation. \\n Special characters: @#\$%^&*()_+ñÑ¿?áéíóúÁÉÍÓÚ"
        val card = Card("Red Hot Chilli Peppers", articleText, "infoUrl")
        val expectedDescription = "${header}${articleText.replace("\\n", "\n").replace("\n", "<br>").replace(card.artistName, "<b>${card.artistName.uppercase(Locale.getDefault())}</b>").replace("'", " ")}${footer}"

        val resultDescription = cardBiographyHelper.getDescription(card)

        println(resultDescription)
        assertEquals(expectedDescription, resultDescription)
    }

    @Test
    fun `get description, all replacements made`(){
        val articleText = "Hello, this is a description of the Red Hot Chilli Peppers. They're the best.\n " +
                "Psychic spies from China try to steal your mind's elation."
        val artistName = "Red Hot Chilli Peppers"
        val card = Card(artistName, articleText, "infoUrl")

        val resultDescription = cardBiographyHelper.getDescription(card)

        assertTrue(!resultDescription.contains("\\n"))
        assertTrue(!resultDescription.contains("\n"))
        assertTrue(!resultDescription.contains(artistName))
        assertTrue(!resultDescription.contains("'"))
    }
}