package ayds.songinfo.moredetails.presentation

data class MoreDetailsUIState(
    val artistName: String,
    val articleBiography: String,
    val articleURL: String,
    val actionsEnabled: Boolean,
    val imageUrl: String =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
)