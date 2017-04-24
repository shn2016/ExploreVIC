package jzha442.explorevic.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

import jzha442.explorevic.utility.Timeconversion;
import jzha442.explorevic.utility.Utils;

import static jzha442.explorevic.utility.Timeconversion.unix2time;

/**
 * Created by Jiao on 24/03/2017.
 */

public class Place {
    //table
    public static final String TABLE="Place";

    //attributes' name in database
    public static final String KEY_ID="id";
    public static final String KEY_name="name";
    public static final String KEY_desc="desc";
    public static final String KEY_facility="facility";
    public static final String KEY_latitude="latitude";
    public static final String KEY_longitude="longitude";
    public static final String KEY_region="region";
    public static final String KEY_suburb="suburb";
    public static final String KEY_street="street";
    public static final String KEY_postcode="postcode";
    public static final String KEY_open_time="open_time";
    public static final String KEY_close_time="close_time";
    public static final String KEY_weekday="weekday";
    public static final String KEY_weekend="weekend";
    public static final String KEY_holiday="holiday";
    public static final String KEY_website="website";
    public static final String KEY_pix="pix";

    //attributes
    private int ID;
    private String name;
    private String desc;
    private String facility;
    private double latitude;
    private double longitude;
    private String region;
    private String suburb;
    private String street;
    private String postcode;
    private int open_time;
    private int close_time;
    private boolean weekday;
    private boolean weekend;
    private boolean holiday;
    private String website;
    private Bitmap pix;

    //constructor

    public Place() {
    }

    public Place(int ID, String name, String desc, String facility, double latitude, double longitude, String region, String suburb, String street, String postcode, String open_time, String close_time, int weekday, int weekend, int holiday, String website, Bitmap pix) {
        this.ID = ID;
        this.name = name;
        this.desc = desc;
        this.facility = facility;
        this.latitude = latitude;
        this.longitude = longitude;
        this.region = region;
        this.suburb = suburb;
        this.street = street;
        this.postcode = postcode;

        if(open_time!=null && !open_time.trim().isEmpty()) {
            this.open_time = Timeconversion.time2unix(open_time).intValue();
        }
        if(close_time!=null && !close_time.trim().isEmpty()) {
            this.close_time = Timeconversion.time2unix(close_time).intValue();
        }
        this.weekday = int2bool(weekday);
        this.weekend = int2bool(weekend);
        this.holiday = int2bool(holiday);
        this.website = website;
        this.pix = pix;
    }

    public Place(int ID, String name, String desc, String facility, double latitude, double longitude, String region, String suburb, String street, String postcode, int open_time, int close_time, boolean weekday, boolean weekend, boolean holiday, String website, Bitmap pix) {
        this.ID = ID;
        this.name = name;
        this.desc = desc;
        this.facility = facility;
        this.latitude = latitude;
        this.longitude = longitude;
        this.region = region;
        this.suburb = suburb;
        this.street = street;
        this.postcode = postcode;
        this.open_time = open_time;
        this.close_time = close_time;
        this.weekday = weekday;
        this.weekend = weekend;
        this.holiday = holiday;
        this.website = website;
        this.pix = pix;
    }

    //getter and setter


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
        this.name = name.trim();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc.trim();
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility.trim();
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region.trim();
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb.trim();
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street.trim();
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode.trim();
    }

    public  String getAddress(){
        String address = null;
        if (street!=null && !street.isEmpty()){
            address = getStreet();
        }
        if (suburb!=null && !suburb.isEmpty()){
            if(address!=null && !address.isEmpty()) {
                address += ", ";
            }
            address += suburb;
        }
        if (postcode!=null && !postcode.isEmpty()){
            if(address!=null && !address.isEmpty()) {
                address += ", ";
            }
            address += postcode;
        }

        return address;
    }

    public int getOpen_time() {
        return open_time;
    }

    public void setOpen_time(int open_time) {
        this.open_time = open_time;
    }

    public int getClose_time() {
        return close_time;
    }

    public void setClose_time(int close_time) {
        this.close_time = close_time;
    }

    public boolean isWeekday() {
        return weekday;
    }

    public void setWeekday(boolean weekday) {
        this.weekday = weekday;
    }

    public boolean isWeekend() {
        return weekend;
    }

    public void setWeekend(boolean weekend) {
        this.weekend = weekend;
    }

    public boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    public String getOpeningTime(){
        String opening_time=null;

        String time = null;
        if(open_time>0 && close_time>0){
            time = Timeconversion.unix2time(new Long(open_time))
                    + " - " +
                    Timeconversion.unix2time(new Long(close_time));
        }

        String day = null;
        if (weekday && !weekend){
            day = "Mon - Fri";
        }else if (weekday && weekend){
            day = "Mon - Sun";
        }

        opening_time = "Opening: ";

        if (weekday && weekend && holiday && time == null){
            opening_time += "All days";
        }else {
            opening_time += day;
            if (day != null && time != null) {
                opening_time += " " + time;
            }
            if (!holiday) {
                opening_time += " (closed at holidays)";
            }
        }

        return opening_time;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Bitmap getPix() {
        return pix;
    }

    public void setPix(Bitmap pix) {
        this.pix = pix;
    }

    //print place to string

    @Override
    public String toString() {
        return "Place{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", facility='" + facility + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", region='" + region + '\'' +
                ", suburb='" + suburb + '\'' +
                ", street='" + street + '\'' +
                ", postcode='" + postcode + '\'' +
                ", open_time=" + open_time +
                ", close_time=" + close_time +
                ", weekday=" + weekday +
                ", weekend=" + weekend +
                ", holiday=" + holiday +
                ", website='" + website + '\'' +
                ", pix=" + pix +
                '}';
    }

    //convert between boolean value and integer - 1 is true, 0 is false

    public int bool2int(boolean b){
        if(b){
            return 1;
        }else {
            return 0;
        }
    }

    public boolean int2bool(int i){
        if(i == 1){
            return true;
        }else {
            return false;
        }
    }

    public ArrayList<String> getSingleFacility(){
        /*
        String[] facilityArray = facility.split(",");
        ArrayList<String> faciArray = new ArrayList<String>();
        for(String f:facilityArray){
            if(f != null && !f.isEmpty() && !f.trim().isEmpty()) {
                faciArray.add(f.trim());
            }
        }
        return faciArray;*/
        return Utils.getSingleElement(facility);
    }
}
