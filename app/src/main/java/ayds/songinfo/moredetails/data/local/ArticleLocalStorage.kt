package ayds.songinfo.moredetails.data.local

import ayds.songinfo.moredetails.domain.Card

interface ArticleLocalStorage{
    fun insertArticle(card: Card)
    fun getArticleByArtistName(artistName: String): Card?
}

internal class ArticleLocalStorageImpl(dataBase: ArticleDatabase) : ArticleLocalStorage {
    private val articleDao: ArticleDao = dataBase.ArticleDao()
    override fun insertArticle(card: Card) {
        articleDao.insertArticle(card.toArticleEntity())
    }

    override fun getArticleByArtistName(artistName: String): Card? {
        return articleDao.getArticleByArtistName(artistName)?.toArticle()
    }

    private fun Card.toArticleEntity() = ArticleEntity(
        this.artistName,
        this.description,
        this.infoUrl
    )

    private fun ArticleEntity.toArticle() = Card(
        this.artistName,
        this.biography,
        this.articleUrl
    )


}