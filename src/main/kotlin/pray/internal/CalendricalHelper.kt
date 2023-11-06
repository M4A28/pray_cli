package pray.internal

import java.util.*

internal object CalendricalHelper {
    /**
     * The Julian Day for a given date
     *
     * @param date the date
     * @return the julian day
     */
     fun julianDay(date: Date?): Double {
        val calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.setTime(date)
        return julianDay(
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH] + 1, calendar[Calendar.DAY_OF_MONTH],
            calendar[Calendar.HOUR_OF_DAY] + calendar[Calendar.MINUTE] / 60.0
        )
    }
    /**
     * The Julian Day for a given Gregorian date
     *
     * @param year  the year
     * @param month the month
     * @param day   the day
     * @param hours hours
     * @return the julian day
     */
    /**
     * The Julian Day for a given Gregorian date
     *
     * @param year  the year
     * @param month the month
     * @param day   the day
     * @return the julian day
     */

    fun julianDay(year: Int, month: Int, day: Int, hours: Double = 0.0): Double {
        /* Equation from Astronomical Algorithms page 60 */

        // NOTE: Integer conversion is done intentionally for the purpose of decimal truncation
        val y = if (month > 2) year else year - 1
        val m = if (month > 2) month else month + 12
        val d = day + hours / 24
        val a = y / 100
        val b = 2 - a + a / 4
        val i0 = (365.25 * (y + 4716)).toInt()
        val i1 = (30.6001 * (m + 1)).toInt()
        return i0 + i1 + d + b - 1524.5
    }

    /**
     * Julian century from the epoch.
     *
     * @param julianDate the julian day
     * @return the julian century from the epoch
     */
    fun julianCentury(julianDate: Double): Double {
        /* Equation from Astronomical Algorithms page 163 */
        return (julianDate - 2451545.0) / 36525
    }
}
