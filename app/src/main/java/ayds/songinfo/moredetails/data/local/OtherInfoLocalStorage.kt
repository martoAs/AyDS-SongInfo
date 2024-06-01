package ayds.songinfo.moredetails.data.local

import ayds.songinfo.moredetails.domain.Card

interface OtherInfoLocalStorage{
    fun insertCard(card: Card)
    fun getCards(artistName: String): List<Card>
}

internal class OtherInfoLocalStorageImpl(dataBase: CardDatabase) : OtherInfoLocalStorage {
    private val cardDao: CardDao = dataBase.cardDao()
    override fun insertCard(card: Card) {
        cardDao.insertCard(card.toCardEntity())
    }

    override fun getCards(artistName: String): List<Card> {
        val cards = cardDao.getCardsByArtistName(artistName)
        return cards.map { it.toCard() }
    }

    private fun Card.toCardEntity() = CardEntity(
        this.artistName,
        this.description,
        this.infoUrl,
        this.source,
        this.sourceLogoUrl
    )

    private fun CardEntity.toCard() = Card(
        this.artistName,
        this.content,
        this.url,
        this.source,
        this.sourceLogoUrl


    )


}