package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song
import kotlinx.datetime.Month

interface SongDateFormatter{
    fun getDate() : String
}
class SongDateFormatterCreator(val song:Song.SpotifySong) {

    private val precision = song.releaseDatePrecision

    fun getDate() =  when (precision) {
                     "year" -> Year(song).getDate()
                     "month" -> Month(song).getDate()
                     "day" -> Day(song).getDate()
                      else -> DefaultFormat(song).getDate() }
}

internal class Day(song:Song.SpotifySong) : SongDateFormatter{
    private val listaFecha = song.releaseDate.split("-")
    override fun getDate() = listaFecha[2] + "/" + listaFecha[1] + "/" + listaFecha[0]
}

internal class Month(song:Song.SpotifySong) : SongDateFormatter{
    private val listaFecha = song.releaseDate.split("-")

    override fun getDate():String{
        var mes = Month(listaFecha[1].toInt()).name
        mes = mes.lowercase()
        mes = mes.replaceFirstChar { char -> char.uppercase() }
        mes + ", " + listaFecha[0]
        return mes
    }
}

internal class Year(song:Song.SpotifySong) : SongDateFormatter{
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

internal class DefaultFormat(var song:Song.SpotifySong) : SongDateFormatter{
    override fun getDate() = song.releaseDate
}
