package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Article

import org.junit.Test
import org.junit.Assert.assertEquals

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
}