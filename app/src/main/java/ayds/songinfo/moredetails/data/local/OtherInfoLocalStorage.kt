package ayds.songinfo.moredetails.data.local

import ayds.songinfo.moredetails.domain.Card

interface OtherInfoLocalStorage{
    fun insertCard(card: Card)
    fun getArticleByArtistName(artistName: String): Card?
}

internal class OtherInfoLocalStorageImpl(dataBase: CardDatabase) : OtherInfoLocalStorage {
    private val cardDao: CardDao = dataBase.cardDao()
    override fun insertCard(card: Card) {
        cardDao.insertCard(card.toArticleEntity())
    }

    override fun getArticleByArtistName(artistName: String): Card? {
        return cardDao.getCardByArtistName(artistName)?.toArticle()
    }

    private fun Card.toArticleEntity() = CardEntity(
        this.artistName,
        this.description,
        this.infoUrl,
        0
    )

    private fun CardEntity.toArticle() = Card(
        this.artistName,
        this.biography,
        this.url
    )


}