package pray.internal;

/**
 * internal.Coordinates representing a particular place
 */
public class Coordinates {

    /**
     * Latitude
     */
    public final double latitude;

    /**
     * Longitude
     */
    public final double longitude;

    /**
     * Generate a new internal.Coordinates object
     *
     * @param latitude  the latitude of the place
     * @param longitude the longitude of the place
     */
    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
