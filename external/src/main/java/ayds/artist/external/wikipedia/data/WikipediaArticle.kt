package ayds.artist.external.wikipedia.data

sealed class WikipediaArticle{
    data class WikipediaArticleWithData(
        var artistName: String,
        var description: String,
        var wikipediaURL: String,
        var wikipediaLogoURL: String
    ): WikipediaArticle()

    data object EmptyWikipediaArticle: WikipediaArticle()
}


