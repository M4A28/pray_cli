import pray.internal.Coordinates
import pray.*
import pray.data.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.*

class Main


fun main(args: Array<String>) {

    if(args.contains("help") || args.size < 3 || args.isEmpty()){
        println(ANSI_RED, "try: java -jar prat_cli.jar <lat> <lang> <methode>")
        println(ANSI_GREEN, "eg: java -jar prat_cli.jar 30.54 31.5 UMM_AL_QURA")
        showMethod()
        return
    }

    val lat = args[0].toDouble()
    val lang = args[1].toDouble()
    val method = args[2]
    val now = Date()
    val coordinates = Coordinates(lat, lang)
    val dateComponents: DateComponents = DateComponents.from(now)
    val parameters = methodParameter(method)
    val timings = PrayerTimes(coordinates, dateComponents, parameters)
    val hijri = HijrahDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    println("------------------------------")
    println(ANSI_PURPLE, dayName(now, 30))
    println("------------------------------")
    print(ANSI_GREEN, formatter(1, now) )
    print(ANSI_RED, "    |    ")
    println(ANSI_GREEN, hijri)
    println("------------------------------")
    println(ANSI_BLUE, "Fajr: ${formatter(3, timings.fajr)}")
    println(ANSI_BLUE, "Sunrise: ${formatter(3, timings.sunrise)}")
    println(ANSI_BLUE, "Dhuhr: ${formatter(3,timings.dhuhr)}")
    println(ANSI_BLUE, "Asr: ${formatter(3,timings.asr)}")
    println(ANSI_BLUE, "Maghrib: ${formatter(3,timings.maghrib)}")
    println(ANSI_BLUE, "Isha: ${formatter(3,timings.isha)}")
    println( "------------------------------")
    println(ANSI_GREEN, "Qibla Direction: ${DecimalFormat("#.###").format(Qibla(coordinates).direction)}")
    println(ANSI_WHITE, "------------------------------")
    println(ANSI_YELLOW, "Current pray is: ${timings.currentPrayer().name.lowercase()}")
    println("------------------------------")
    printEvent()
    println("------------------------------")
}

fun methodParameter(m: String): CalculationParameters{
    return when(m.uppercase()){
        "MUSLIM_WORLD_LEAGUE" -> CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters
        "EGYPTIAN" -> CalculationMethod.EGYPTIAN.parameters
        "KARACHI" -> CalculationMethod.KARACHI.parameters
        "UMM_AL_QURA" -> CalculationMethod.UMM_AL_QURA.parameters
        "NORTH_AMERICA" -> CalculationMethod.NORTH_AMERICA.parameters
        else -> CalculationMethod.UMM_AL_QURA.parameters
    }
}

fun printEvent(){
    val currentHijrahDate = HijrahDate.now()
    val year = DateTimeFormatter.ofPattern("yyyy").format(currentHijrahDate).toInt()
    val ramadan = daysUntilEvent(HijrahDate.of(year, 9, 1))
    val faterEad = daysUntilEvent(HijrahDate.of(year, 10, 1))
    val adahEad = daysUntilEvent(HijrahDate.of(year, 12, 10))
    val nextFriday = nextFriday(LocalDateTime.now())
    if(ramadan < 90 && ramadan > -1){
        if(ramadan == 0)
            println(ANSI_CYAN, "✳\uFE0F ✳\uFE0F Ramdan Mumabrak ✳\uFE0F ✳\uFE0F")
        else
            println(ANSI_CYAN, "Days until Ramadan: $ramadan")
        return
    }
    else if(faterEad < 10 && faterEad > -1){
        if(faterEad == 0)
            println(ANSI_CYAN, "✳\uFE0F ✳\uFE0F Ead Mumabrak ✳\uFE0F ✳\uFE0F")
        else
            println(ANSI_CYAN, "Days until Ft-ra Ead: $faterEad")
        return
    }
    else if (adahEad < 15 && adahEad > -1){
        if(adahEad == 0)
            println(ANSI_CYAN, "✳\uFE0F ✳\uFE0F Ead Mumabrak ✳\uFE0F ✳\uFE0F")
        else
            println(ANSI_CYAN, "Days until Ad aha Ead: $adahEad")
        return
    }
    else
        println(ANSI_CYAN, "Next Friday: ${formatter(1, nextFriday)}")

}

fun formatter(case: Int, date: Date): String{
    return when(case){
        1 -> SimpleDateFormat("yyyy-MM-dd").format(date)
        2 -> SimpleDateFormat("HH:mm ss").format(date)
        3 -> SimpleDateFormat("HH:mm").format(date)
        4 -> SimpleDateFormat("hh:mm ss a").format(date)
        5 -> SimpleDateFormat("EEEE").format(date)
        else -> SimpleDateFormat("yyyy-MM-dd hh:mm ss a").format(date)
    }
}

fun daysUntilEvent(eventDate: HijrahDate): Int {
    val currentHijrahDate = HijrahDate.now()
    val event = eventDate.toEpochDay() - currentHijrahDate.toEpochDay()
    return event.toInt()
}

fun nextFriday(date: LocalDateTime): Date {
    return Date.from(
        date.with(
            TemporalAdjusters.next(DayOfWeek.FRIDAY)
        )
            .toInstant(ZoneOffset.ofTotalSeconds(0))
    )//date.with(TemporalAdjusters.next(DayOfWeek.FRIDAY))
}

fun dayName(date: Date, size: Int): String{
    val dateName = formatter(5, date)
    return " ".repeat(((size-dateName.length)/2)) + dateName
}

fun showMethod(){
    val methods = listOf("UMM_AL_QURA" ,
        "MUSLIM_WORLD_LEAGUE",
        "EGYPTIAN" ,
        "KARACHI" ,
        "NORTH_AMERICA"
    )
    var num = 1
    methods.forEach{
        println("$num. $it")
        num++
    }
}


