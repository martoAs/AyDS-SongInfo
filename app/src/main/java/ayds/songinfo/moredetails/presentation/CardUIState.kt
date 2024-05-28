package ayds.songinfo.moredetails.presentation

data class CardUIState(
    val artistName: String,
    val contentHtml: String,
    val url: String,
    val actionsEnabled: Boolean,
    val imageUrl: String,
    val source: String

)