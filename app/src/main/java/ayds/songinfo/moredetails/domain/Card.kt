package ayds.songinfo.moredetails.domain

data class Card(
    val artistName: String,
    val description: String,
    val infoUrl: String,
    val source: CardSource,
    val sourceLogoUrl: String,
    var isLocallyStored: Boolean = false)

enum class CardSource(val sourceName: String) {
    NYTIMES("NY Times"),
    WIKIPEDIA("Wikipedia"),
    LASTFM("LastFM")
}