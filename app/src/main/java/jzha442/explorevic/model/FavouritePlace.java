package jzha442.explorevic.model;

import android.graphics.Bitmap;

/**
 * Created by Jiao on 10/04/2017.
 */

public class FavouritePlace {
    //table
    public static final String TABLE="Favourite_Places";

    //attributes' name in database
    public static final String KEY_ID="id";
    public static final String KEY_name="name";
    public static final String KEY_desc="desc";
    public static final String KEY_region="region";
    public static final String KEY_address="address";
    public static final String KEY_latitude="latitude";
    public static final String KEY_longitude="longitude";
    public static final String KEY_activity="activity";
    public static final String KEY_facility="facility";
    public static final String KEY_pix="pix";
    public static final String KEY_ap_id="ap_id";

    //attributes
    private int ID;
    private String name;
    private String desc;
    private String region;
    private String address;
    private double latitude;
    private double longitude;
    private String activity;
    private String facility;
    private Bitmap pix;
    private int ap_id = -1;

    public FavouritePlace() {
    }

    public FavouritePlace(int ap_id) {
        this.ap_id = ap_id;
    }

    public FavouritePlace(String name, String desc, String region, String address, String activity, String facility, Bitmap pix) {
        this.name = name;
        this.desc = desc;
        this.region = region;
        this.address = address;
        this.activity = activity;
        this.facility = facility;
        this.pix = pix;
    }

    public FavouritePlace(String name, String desc, String region, String address, double latitude, double longitude, String activity, String facility, Bitmap pix) {
        this.name = name;
        this.desc = desc;
        this.region = region;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.activity = activity;
        this.facility = facility;
        this.pix = pix;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public Bitmap getPix() {
        return pix;
    }

    public void setPix(Bitmap pix) {
        this.pix = pix;
    }

    public int getAp_id() {
        return ap_id;
    }

    public void setAp_id(int ap_id) {
        this.ap_id = ap_id;
    }
}
