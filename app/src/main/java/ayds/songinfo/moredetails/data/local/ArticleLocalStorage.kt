package ayds.songinfo.moredetails.data.local

import ayds.songinfo.moredetails.domain.Article.ArtistArticle

interface ArticleLocalStorage{
    fun insertArticle(article: ArtistArticle)
    fun getArticleByArtistName(artistName: String): ArtistArticle?
}

internal class ArticleLocalStorageImpl(dataBase: ArticleDatabase) : ArticleLocalStorage {
    private val articleDao: ArticleDao = dataBase.ArticleDao()
    override fun insertArticle(article: ArtistArticle) {
        articleDao.insertArticle(article.toArticleEntity())
    }

    override fun getArticleByArtistName(artistName: String): ArtistArticle? {
        return articleDao.getArticleByArtistName(artistName)?.toArtistArticle()
    }

    private fun ArtistArticle.toArticleEntity() = ArticleEntity(
        this.artistName,
        this.biography,
        this.articleUrl
    )

    private fun ArticleEntity.toArtistArticle() = ArtistArticle(
        this.artistName,
        this.biography,
        this.articleUrl
    )


}