package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song
import kotlinx.datetime.Month

interface SongDateFormatterFactory{
    fun getSongDateFormatter(song: Song.SpotifySong): SongDateFormatter
}

class SongDateFormatterFactoryImpl: SongDateFormatterFactory{
    override fun getSongDateFormatter(song: Song.SpotifySong) =
        when (song.releaseDatePrecision) {
            "year" -> Year(song)
            "month" -> Month(song)
            "day" -> Day(song)
            else -> DefaultFormat(song)
        }
}

interface SongDateFormatter{
    val song:Song.SpotifySong
    fun getDate() : String
}

internal class Day(override val song:Song.SpotifySong): SongDateFormatter{
    private val listaFecha = song.releaseDate.split("-")
    override fun getDate() = listaFecha[2] + "/" + listaFecha[1] + "/" + listaFecha[0]
}

internal class Month(override val song:Song.SpotifySong) : SongDateFormatter{
    private val listaFecha = song.releaseDate.split("-")

    override fun getDate():String{
        var mes = Month(listaFecha[1].toInt()).name
        mes = mes.lowercase()
        mes = mes.replaceFirstChar { char -> char.uppercase() }
        mes + ", " + listaFecha[0]
        return mes
    }
}

internal class Year(override val song:Song.SpotifySong) : SongDateFormatter{
    private val listaFecha = song.releaseDate.split("-")
    override fun getDate(): String {
        val year = listaFecha[0].toInt()
        val isLeapYear = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))
        return if(isLeapYear)
            "$year (leap year)"
        else
            "$year (not a leap year)"
    }

}

internal class DefaultFormat(override val  song:Song.SpotifySong) : SongDateFormatter{
    override fun getDate() = song.releaseDate
}
