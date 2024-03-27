package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.model.entities.Song.EmptySong
import ayds.songinfo.home.model.entities.Song.SpotifySong
import kotlinx.datetime.Month
interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl : SongDescriptionHelper {
    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Release date: ${releaseDateFormatter(song)}"
            else -> "Song not found"
        }
    }

    private fun releaseDateFormatter(song:SpotifySong):String{
        val listaFecha = song.releaseDate.split("-")
        var formattedDate = ""
        when(song.releaseDatePrecision){
            "year" -> formattedDate = listaFecha[0] /* TODO: agregar leap year */
            "month" -> {
            var mes = Month(listaFecha[1].toInt()).name
            mes = mes.lowercase()
            mes = mes.replaceFirstChar { char -> char.uppercase() }
            formattedDate = mes + ", " + listaFecha[0]
            }
            "day" -> formattedDate = listaFecha[2] + "/" + listaFecha[1] + "/" + listaFecha[0]
        }
        return formattedDate
    }


}