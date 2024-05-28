package ayds.songinfo.moredetails.domain

data class Card(
    val artistName: String,
    val description: String,
    val infoUrl: String,
    val source: CardSource = CardSource.LASTFM,
    val sourceLogoUrl: String = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png",
    var isLocallyStored: Boolean = false)

enum class CardSource(val sourceName: String) {
    NYTIMES("NY Times"),
    WIKIPEDIA("Wikipedia"),
    LASTFM("LastFM")
}