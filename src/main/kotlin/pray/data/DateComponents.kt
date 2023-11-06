package pray.data

import java.util.*

class DateComponents( val year: Int,  val month: Int,  val day: Int) {
    companion object {
        /**
         * Convenience method that returns a DateComponents from a given Date
         *
         * @param date the date
         * @return the DateComponents (according to the default device timezone)
         */
        fun from(date: Date?): DateComponents {
            val calendar = GregorianCalendar.getInstance()
            calendar.setTime(date)
            return DateComponents(
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH] + 1, calendar[Calendar.DAY_OF_MONTH]
            )
        }

        /**
         * Convenience method that returns a DateComponents from a given
         * Date that was constructed from UTC based components
         *
         * @param date the date
         * @return the DateComponents (according to UTC)
         */
        fun fromUTC(date: Date?): DateComponents {
            val calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.setTime(date)
            return DateComponents(
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH] + 1, calendar[Calendar.DAY_OF_MONTH]
            )
        }
    }
}
