package ayds.songinfo.moredetails.data.local

import ayds.songinfo.moredetails.domain.Card

interface OtherInfoLocalStorage{
    fun insertCard(card: Card)
    fun getCard(artistName: String): Card?
}

internal class OtherInfoLocalStorageImpl(dataBase: CardDatabase) : OtherInfoLocalStorage {
    private val cardDao: CardDao = dataBase.cardDao()
    override fun insertCard(card: Card) {
        cardDao.insertCard(card.toCardEntity())
    }

    override fun getCard(artistName: String): Card? {
        return cardDao.getCardByArtistName(artistName)?.toCard()
    }

    private fun Card.toCardEntity() = CardEntity(
        this.artistName,
        this.description,
        this.infoUrl,
        this.source.sourceName
    )

    private fun CardEntity.toCard() = Card(
        this.artistName,
        this.content,
        this.url


    )


}