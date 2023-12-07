package pray.data

import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.*

object TimeFactory {
    fun timeUntil(next: Date): String {
        val currentTime = Date()
        val timeInSecond = (next.time - currentTime.time) / 1000L
        val timeInMint = timeInSecond / 60L % 60
        val hour = timeInSecond / 60L / 60 % 60
        return "${hour}h:${timeInMint}m"
    }
    fun dayName(date: Date, textSize: Int): String {
        val dateName = formatter(5, date)
        return " ".repeat(((textSize - dateName.length) / 2)) + dateName
    }
    fun dayName(date: Date): String {
        return formatter(5, date)
    }

    fun nextFriday(date: LocalDateTime): Date {
        return java.sql.Date.valueOf(
            (date.with(TemporalAdjusters.next(DayOfWeek.FRIDAY)).toLocalDate())
        )
    }

    fun nextDayDateByName(date: LocalDateTime, dayName:DayOfWeek): Date{
        return java.sql.Date.valueOf(
            (date.with(TemporalAdjusters.next(dayName)).toLocalDate())
        )
    }

    fun timeUntilEvent(event: HijrahDate): String {
        val currentDate = HijrahDate.now().toEpochDay() * 24L*60*60*1000L
        val eventDate = event.toEpochDay() * 24L*60*60*1000L
        val diff = eventDate - currentDate
        val days = diff / (24 * 60 * 60 * 1000)
        val hours = diff /(60 * 60 * 1000) % 24
        val minutes = diff /(60 * 1000) % 60
        val seconds = diff / 1000 % 60
        return "$days Days $hours Hour $minutes Minute $seconds S"
    }
    fun timeUntilEvent(event: Date): String {
        val currentDate = Date().time
        val eventDate = event.time
        val diff = eventDate - currentDate
        val days = diff / (24 * 60 * 60 * 1000)
        val hours = diff /(60 * 60 * 1000) % 24
        val minutes = diff /(60 * 1000) % 60
        val seconds = diff / 1000 % 60
        return "$days Days $hours Hour $minutes Minute $seconds S"
    }
    fun daysUntilEvent(eventDate: HijrahDate): Int {
        val currentHijriDate = HijrahDate.now()
        val event = eventDate.toEpochDay() - currentHijriDate.toEpochDay()
        return event.toInt()
    }
    fun daysUntilEvent(eventDate: Date): Int {
        val currentDate = Date()
        val  diff= eventDate.time - currentDate.time
        return  (diff / (24 * 60 * 60 * 1000)).toInt()
    }
    fun formatter(case: Int, date: Date): String {
        return when (case) {
            1 -> SimpleDateFormat("yyyy-MM-dd").format(date)
            2 -> SimpleDateFormat("HH:mm ss").format(date)
            3 -> SimpleDateFormat("HH:mm").format(date)
            4 -> SimpleDateFormat("hh:mm ss a").format(date)
            5 -> SimpleDateFormat("EEEE").format(date)
            else -> SimpleDateFormat("yyyy-MM-dd hh:mm ss a").format(date)
        }
    }

    fun islamicEvent(): Map<String, HijrahDate>{
        val currentDate = HijrahDate.now()
        val events = mutableMapOf<String, HijrahDate>()
        val year = DateTimeFormatter.ofPattern("yyyy").format(currentDate).toInt()
        val month = DateTimeFormatter.ofPattern("M").format(currentDate).toInt()
        val day = DateTimeFormatter.ofPattern("d").format(currentDate).toInt()

        val ashora = HijrahDate.of(
            if(month in 2..12)
                year + 1 else year, 1, 10)
        val ramadan = HijrahDate.of(
            if(month in 1..8)
                year else year + 1, 9, 1)
        val faterEad = HijrahDate.of(
            if(month in 1..9)
                year else year + 1, 10, 1)
        val arfa = HijrahDate.of(
            if(month == 12 && day > 9)
                year + 1 else year, 12, 9)
        val adahEad = HijrahDate.of(
            if(month == 12 && day > 11)
                year + 1 else year , 12, 10)

        events["ashra"] = ashora
        events["Ramdan"] = ramadan
        events["Eaid Fetra"] = faterEad
        events["Yum Araf"] = arfa
        events["Eaid Adah"] = adahEad
        return events
    }

    fun hijriDateFormatetr(date: HijrahDate): String{
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date)
    }
}