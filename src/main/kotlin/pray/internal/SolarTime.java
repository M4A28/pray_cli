package pray.internal;


import pray.data.DateComponents;

public class SolarTime {

    public final double transit;
    public final double sunrise;
    public final double sunset;

    private final Coordinates observer;
    private final SolarCoordinates solar;
    private final SolarCoordinates prevSolar;
    private final SolarCoordinates nextSolar;
    private final double approximateTransit;

    public SolarTime(DateComponents today, Coordinates coordinates) {
        final double julianDate = CalendricalHelper.julianDay(today.year, today.month, today.day);

        this.prevSolar = new SolarCoordinates(julianDate - 1);
        this.solar = new SolarCoordinates(julianDate);
        this.nextSolar = new SolarCoordinates(julianDate + 1);

        this.approximateTransit = Astronomical.approximateTransit(coordinates.longitude,
                solar.apparentSiderealTime, solar.rightAscension);
        final double solarAltitude = -50.0 / 60.0;

        this.observer = coordinates;
        this.transit = Astronomical.correctedTransit(this.approximateTransit, coordinates.longitude,
                solar.apparentSiderealTime, solar.rightAscension, prevSolar.rightAscension,
                nextSolar.rightAscension);
        this.sunrise = Astronomical.correctedHourAngle(this.approximateTransit, solarAltitude,
                coordinates, false, solar.apparentSiderealTime, solar.rightAscension,
                prevSolar.rightAscension, nextSolar.rightAscension, solar.declination,
                prevSolar.declination, nextSolar.declination);
        this.sunset = Astronomical.correctedHourAngle(this.approximateTransit, solarAltitude,
                coordinates, true, solar.apparentSiderealTime, solar.rightAscension,
                prevSolar.rightAscension, nextSolar.rightAscension, solar.declination,
                prevSolar.declination, nextSolar.declination);
    }

    public double hourAngle(double angle, boolean afterTransit) {
        return Astronomical.correctedHourAngle(this.approximateTransit, angle, this.observer,
                afterTransit, this.solar.apparentSiderealTime, this.solar.rightAscension,
                this.prevSolar.rightAscension, this.nextSolar.rightAscension, this.solar.declination,
                this.prevSolar.declination, this.nextSolar.declination);
    }

    // hours from transit
    public double afternoon(ShadowLength shadowLength) {
        // TODO (from Swift version) source shadow angle calculation
        final double tangent = Math.abs(observer.latitude - solar.declination);
        final double inverse = shadowLength.getShadowLength() + Math.tan(Math.toRadians(tangent));
        final double angle = Math.toDegrees(Math.atan(1.0 / inverse));

        return hourAngle(angle, true);
    }

}
