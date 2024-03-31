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
        //var formattedDate = ""
        return when(song.releaseDatePrecision){
            "year" -> {
                val year = listaFecha[0].toInt()
                if(year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))
                    "$year (leap year)"
                else
                    "$year (not a leap year)"

            }
            "month" -> {
            var mes = Month(listaFecha[1].toInt()).name
            mes = mes.lowercase()
            mes = mes.replaceFirstChar { char -> char.uppercase() }
            mes + ", " + listaFecha[0]
            }
            "day" -> listaFecha[2] + "/" + listaFecha[1] + "/" + listaFecha[0]
            else -> {song.releaseDate}
        }

    }


}