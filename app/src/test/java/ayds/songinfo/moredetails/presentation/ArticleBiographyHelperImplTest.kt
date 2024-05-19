package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Article

import org.junit.Test
import org.junit.Assert.assertEquals

class ArticleBiographyHelperImplTest(){

    private val articleBiographyHelper: ArticleBiographyHelper = ArticleBiographyHelperImpl()
    private val footer = "</font></div></html>"
    private val header = "<html><div width=400><font face=\"arial\">"
    @Test
    fun getDescriptionTestWithNoNewLines(){
        val article = Article("Red Hot Chilli Peppers", "Hello, this is a description of the Red Hot Chilli Peppers. They are the best", "infoUrl")
        val expectedDescription = "${header}Hello, this is a description of the <b>RED HOT CHILLI PEPPERS</b>. They are the best${footer}"

        val resultDescription = articleBiographyHelper.getDescription(article)

        assertEquals(expectedDescription, resultDescription)

    }
}