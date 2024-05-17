package ayds.songinfo.moredetails.data.local

import ayds.songinfo.moredetails.domain.Article

interface ArticleLocalStorage{
    fun insertArticle(article: Article)
    fun getArticleByArtistName(artistName: String): Article?
}

internal class ArticleLocalStorageImpl(dataBase: ArticleDatabase) : ArticleLocalStorage {
    private val articleDao: ArticleDao = dataBase.ArticleDao()
    override fun insertArticle(article: Article) {
        articleDao.insertArticle(article.toArticleEntity())
    }

    override fun getArticleByArtistName(artistName: String): Article? {
        return articleDao.getArticleByArtistName(artistName)?.toArticle()
    }

    private fun Article.toArticleEntity() = ArticleEntity(
        this.artistName,
        this.biography,
        this.articleUrl
    )

    private fun ArticleEntity.toArticle() = Article(
        this.artistName,
        this.biography,
        this.articleUrl
    )


}