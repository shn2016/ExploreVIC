package jzha442.explorevic.model;

/**
 * Created by kaigao on 17/4/19.
 */

public class NearbyPlace {
    private String placeName;
    private String vicinity;
    private String latitude;
    private String longitude;
    private String reference ;

    public NearbyPlace() {
        String placeName = "unknown";
        String vicinity = "unknown";
        String latitude = "";
        String longitude = "";
        String reference = "";
    }

    public NearbyPlace(String placeName, String vicinity, String latitude, String longitude, String reference) {
        this.placeName = placeName;
        this.vicinity = vicinity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.reference = reference;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return "NearbyPlace{" +
                "placeName='" + placeName + '\'' +
                ", vicinity='" + vicinity + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", reference='" + reference + '\'' +
                '}';
    }
}
