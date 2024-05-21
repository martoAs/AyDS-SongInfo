package ayds.songinfo.moredetails.presentation

data class MoreDetailsUIState(
    val artistName: String,
    val articleBiography: String,
    val articleURL: String,
    val actionsEnabled: Boolean,
    val imageUrl: String

)