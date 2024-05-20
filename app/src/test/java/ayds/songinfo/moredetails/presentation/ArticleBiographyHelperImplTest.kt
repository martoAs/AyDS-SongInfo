package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Article
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Locale

class ArticleBiographyHelperImplTest {

    private val articleBiographyHelper: ArticleBiographyHelper = ArticleBiographyHelperImpl()
    private val footer = "</font></div></html>"
    private val header = "<html><div width=400><font face=\"arial\">"
    @Test
    fun getDescriptionTest(){
        val articleText = "Hello, this is a description of the Red Hot Chilli Peppers. They're the best.\n " +
                "Psychic spies from China try to steal your mind's elation."
        val article = Article("Red Hot Chilli Peppers", articleText, "infoUrl")
        val expectedDescription = "${header}Hello, this is a description of the <b>RED HOT CHILLI PEPPERS</b>. They re the best.<br> Psychic spies from China try to steal your mind s elation.${footer}"

        val resultDescription = articleBiographyHelper.getDescription(article)

        assertEquals(expectedDescription, resultDescription)
    }

    /* CASOS LÍMITE:
    *      - El artist name nunca es vacío por el flujo de ejecución, no se encontraría una canción por lo que el botón de more details estaría deshabilitado
    *      - La url del article no se utiliza */

    @Test
    fun `get description, article without biography`(){
        val article = Article("artist name", "", "infoUrl")
        val expectedDescription = "${header}${footer}"

        val resultDescription = articleBiographyHelper.getDescription(article)

        assertEquals(expectedDescription, resultDescription)
    }

    @Test
    fun `get description, biography con caracteres especiales`(){
        val articleText = "Hello, this is a description of the Red Hot Chilli Peppers. They're the best.\n " +
                "Psychic spies from China try to steal your mind's elation. \\n Special characters: @#\$%^&*()_+ñ¿?"
        val article = Article("Red Hot Chilli Peppers", articleText, "infoUrl")
        val expectedDescription = "${header}${articleText.replace("\\n", "\n").replace("\n", "<br>").replace(article.artistName, "<b>${article.artistName.uppercase(Locale.getDefault())}</b>").replace("'", " ")}${footer}"

        val resultDescription = articleBiographyHelper.getDescription(article)

        assertEquals(expectedDescription, resultDescription)
    }

    @Test
    fun `get description, all replacements made`(){
        val articleText = "Hello, this is a description of the Red Hot Chilli Peppers. They're the best.\n " +
                "Psychic spies from China try to steal your mind's elation."
        val artistName = "Red Hot Chilli Peppers"
        val article = Article(artistName, articleText, "infoUrl")

        val resultDescription = articleBiographyHelper.getDescription(article)

        assertTrue(!resultDescription.contains("\\n"))
        assertTrue(!resultDescription.contains("\n"))
        assertTrue(!resultDescription.contains(artistName))
        assertTrue(!resultDescription.contains("'"))
    }
}