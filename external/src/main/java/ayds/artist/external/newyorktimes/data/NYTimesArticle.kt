package ayds.artist.external.newyorktimes.data

sealed class NYTimesArticle {

    data class NYTimesArticleWithData(
        val name: String,
        val info: String,
        val url: String,
        val logourl:String = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

    ): NYTimesArticle()

    data object EmptyArtistDataExternal : NYTimesArticle()
}