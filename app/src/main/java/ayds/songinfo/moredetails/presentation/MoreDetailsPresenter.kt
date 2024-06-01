package ayds.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.OtherInfoRepository

interface MoreDetailsPresenter {
    val cardObservable: Observable<List<CardUIState>>
    fun updateCards(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private val repository: OtherInfoRepository,
    private val cardBiographyHelper: CardBiographyHelper
) :
    MoreDetailsPresenter {

    override val cardObservable = Subject<List<CardUIState>>()
    override fun updateCards(artistName: String) {
        val cards = repository.getListOfCards(artistName)
        cardObservable.notify(cards.map { it.toUIState() })
    }

    private fun Card.toUIState() = CardUIState(
        artistName,
        cardBiographyHelper.getDescription(this),
        infoUrl,
        infoUrl != "",
        sourceLogoUrl,
        source.name
    )
}