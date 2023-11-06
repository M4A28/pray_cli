import pray.*
import pray.data.DateComponents
import pray.internal.Coordinates
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.*
import java.sql.Date as SqlDate

class Main

lateinit var timings: PrayerTimes
fun main(args: Array<String>) {
    // TODO: add defaulter sitting to save time
    if (args.contains("help") || args.size < 3 || args.isEmpty()) {
        println(ANSI_RED, "try: java -jar prat_cli.jar <lat> <lang> <methode>")
        println(ANSI_GREEN, "eg: java -jar prat_cli.jar 30.54 31.5 UMM_AL_QURA")
        showCalculationMethod()
        return
    }
    val lat = args[0].toDouble()
    val lang = args[1].toDouble()
    val method = args[2]
    val currentDate = Date()
    val coordinates = Coordinates(lat, lang)
    val dateComponents: DateComponents = DateComponents.from(currentDate)
    val parameters = methodParameter(method)
    timings = PrayerTimes(coordinates, dateComponents, parameters)
    val hijri = HijrahDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val currentPray = timings.currentPrayer()
    val lastThirdOfNight = SunnahTimes(timings).lastThirdOfTheNight
    val nextPrayName = if (timings.nextPrayer().name == "NONE") "fajr" else timings.nextPrayer().name.lowercase()
    // day name
    println("==============================")
    println(ANSI_PURPLE, dayName(currentDate, 30))
    // date and time
    println("------------------------------")
    print(ANSI_GREEN, formatter(1, currentDate))
    print(ANSI_RED, "    |    ")
    println(ANSI_GREEN, hijri)
    println("------------------------------")
    println(ANSI_BLUE, "Current Time: ${formatter(3, currentDate)}")
    println("------------------------------")
    // pray times
    println(
        colorOfCurrentPray(Prayer.FAJR, currentPray),
        "Fajr: ${formatter(3, timings.fajr!!)}"
    )
    println(
        colorOfCurrentPray(Prayer.SUNRISE, currentPray),
        "Sunrise: ${formatter(3, timings.sunrise!!)}"
    )
    println(
        colorOfCurrentPray(Prayer.DHUHR, currentPray),
        "Dhuhr: ${formatter(3, timings.dhuhr!!)}"
    )
    println(
        colorOfCurrentPray(Prayer.ASR, currentPray),
        "Asr: ${formatter(3, timings.asr!!)}"
    )
    println(
        colorOfCurrentPray(Prayer.MAGHRIB, currentPray),
        "Maghrib: ${formatter(3, timings.maghrib!!)}"
    )
    println(
        colorOfCurrentPray(Prayer.ISHA, currentPray),
        "Isha: ${formatter(3, timings.isha!!)}"
    )
    println("------------------------------")
    println(
        ANSI_BLUE,
        "Last third of night: ${formatter(3, lastThirdOfNight)}"
    )
    // next pray
    println("------------------------------")
    println(ANSI_PURPLE, "Next Pray is: $nextPrayName")
    println("------------------------------")
    println(ANSI_PURPLE, "Time until $nextPrayName: " + timeUntil(timings.timeForPrayer(timings.nextPrayer())))
    println("------------------------------")
    println(
        ANSI_GREEN, "Qibla Direction: ${
            DecimalFormat("#.###")
                .format(
                    Qibla(coordinates)
                        .direction
                )
        }"
    )
    println("------------------------------")
    printEvent()
    println("==============================")
}

fun methodParameter(m: String): CalculationParameters {
    return when (m.uppercase()) {
        "MUSLIM_WORLD_LEAGUE" -> CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters
        "EGYPTIAN" -> CalculationMethod.EGYPTIAN.parameters
        "KARACHI" -> CalculationMethod.KARACHI.parameters
        "UMM_AL_QURA" -> CalculationMethod.UMM_AL_QURA.parameters
        "NORTH_AMERICA" -> CalculationMethod.NORTH_AMERICA.parameters
        else -> CalculationMethod.UMM_AL_QURA.parameters
    }
}

fun printEvent() {
    val currentHijrahDate = HijrahDate.now()
    val year = DateTimeFormatter.ofPattern("yyyy").format(currentHijrahDate).toInt()
    val ramadan = daysUntilEvent(HijrahDate.of(year, 9, 1))
    val faterEad = daysUntilEvent(HijrahDate.of(year, 10, 1))
    val adahEad = daysUntilEvent(HijrahDate.of(year, 12, 10))
    val nextFriday = nextFriday(LocalDateTime.now())
    if (ramadan < 90 && ramadan > -1) {
        if (ramadan == 0)
            println(ANSI_CYAN, "✳\uFE0F ✳\uFE0F Ramdan Mumabrak ✳\uFE0F ✳\uFE0F")
        else
            println(ANSI_CYAN, "Days until Ramadan: $ramadan")
        return
    } else if (faterEad < 10 && faterEad > -1) {
        if (faterEad == 0)
            println(ANSI_CYAN, "✳\uFE0F ✳\uFE0F Ead Mumabrak ✳\uFE0F ✳\uFE0F")
        else
            println(ANSI_CYAN, "Days until Ft-ra Ead: $faterEad")
        return
    } else if (adahEad < 15 && adahEad > -1) {
        if (adahEad == 0)
            println(ANSI_CYAN, "✳\uFE0F ✳\uFE0F Ead Mumabrak ✳\uFE0F ✳\uFE0F")
        else
            println(ANSI_CYAN, "Days until Ad aha Ead: $adahEad")
        return
    } else
        println(ANSI_CYAN, "Next Friday: ${formatter(1, nextFriday)}")
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

fun daysUntilEvent(eventDate: HijrahDate): Int {
    val currentHijrahDate = HijrahDate.now()
    val event = eventDate.toEpochDay() - currentHijrahDate.toEpochDay()
    return event.toInt()
}

fun nextFriday(date: LocalDateTime): Date {
    return SqlDate.valueOf(
        (date.with(TemporalAdjusters.next(DayOfWeek.FRIDAY)).toLocalDate())
    )
}

fun dayName(date: Date, size: Int): String {
    val dateName = formatter(5, date)
    return " ".repeat(((size - dateName.length) / 2)) + dateName
}

fun showCalculationMethod() {
    var num = 1
    val methods = listOf(
        "UMM_AL_QURA",
        "MUSLIM_WORLD_LEAGUE",
        "EGYPTIAN",
        "KARACHI",
        "NORTH_AMERICA"
    )
    println("supported methods: ")
    methods.forEach {
        println("$num. $it")
        num++
    }
}

fun timeUntil(next: Date): String {
    val currentTime = Date()
    val timeInSecond = (next.time - currentTime.time) / 1000L
    val timeInMint = timeInSecond / 60L % 60
    val timeInHoure = timeInSecond / 60L / 60 % 60
    return "${timeInHoure}h:${timeInMint}m"
}

fun colorOfCurrentPray(pray: Prayer, currentPray: Prayer): String {
    return if (pray == currentPray) ANSI_BAK_YELLOW else ANSI_BLUE
}



