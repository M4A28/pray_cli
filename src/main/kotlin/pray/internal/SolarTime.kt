package pray.internal

import pray.data.DateComponents
import pray.internal.CalendricalHelper.julianDay
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.tan

class SolarTime(today: DateComponents, coordinates: Coordinates) {
    val transit: Double
    val sunrise: Double
    val sunset: Double
    private val observer: Coordinates
    private val solar: SolarCoordinates
    private val prevSolar: SolarCoordinates
    private val nextSolar: SolarCoordinates
    private val approximateTransit: Double

    init {
        val julianDate: Double = julianDay(today.year, today.month, today.day)
        prevSolar = SolarCoordinates(julianDate - 1)
        solar = SolarCoordinates(julianDate)
        nextSolar = SolarCoordinates(julianDate + 1)
        approximateTransit = Astronomical.approximateTransit(
            coordinates.longitude,
            solar.apparentSiderealTime, solar.rightAscension
        )
        val solarAltitude = -50.0 / 60.0
        observer = coordinates
        transit = Astronomical.correctedTransit(
            approximateTransit, coordinates.longitude,
            solar.apparentSiderealTime, solar.rightAscension, prevSolar.rightAscension,
            nextSolar.rightAscension
        )
        sunrise = Astronomical.correctedHourAngle(
            approximateTransit, solarAltitude,
            coordinates, false, solar.apparentSiderealTime, solar.rightAscension,
            prevSolar.rightAscension, nextSolar.rightAscension, solar.declination,
            prevSolar.declination, nextSolar.declination
        )
        sunset = Astronomical.correctedHourAngle(
            approximateTransit, solarAltitude,
            coordinates, true, solar.apparentSiderealTime, solar.rightAscension,
            prevSolar.rightAscension, nextSolar.rightAscension, solar.declination,
            prevSolar.declination, nextSolar.declination
        )
    }

    fun hourAngle(angle: Double, afterTransit: Boolean): Double {
        return Astronomical.correctedHourAngle(
            approximateTransit, angle, observer,
            afterTransit, solar.apparentSiderealTime, solar.rightAscension,
            prevSolar.rightAscension, nextSolar.rightAscension, solar.declination,
            prevSolar.declination, nextSolar.declination
        )
    }

    // hours from transit
    fun afternoon(shadowLength: ShadowLength): Double {
        // TODO (from Swift version) source shadow angle calculation
        val tangent = abs(observer.latitude - solar.declination)
        val inverse = shadowLength.shadowLength + tan(Math.toRadians(tangent))
        val angle = Math.toDegrees(atan(1.0 / inverse))
        return hourAngle(angle, true)
    }
}
