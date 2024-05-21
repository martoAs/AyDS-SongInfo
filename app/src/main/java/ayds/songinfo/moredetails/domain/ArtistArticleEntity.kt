package ayds.songinfo.moredetails.domain

data class Article(
    val artistName: String,
    val biography: String,
    val articleUrl: String,
    var isLocallyStored: Boolean = false)