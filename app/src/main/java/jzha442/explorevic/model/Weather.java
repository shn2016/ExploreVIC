package jzha442.explorevic.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.google.gson.GsonBuilder;

/**
 * Created by Kai Gao on 17/3/30.
 */

public class Weather {
    //table
    //public static final String TABLE="Weather";

    //attributes' name in database  Save for later use
    //public static final String KEY_ID="id";
    //public static final String KEY_name="type";
    //public static final String KEY_desc="windSpeed";//facility needed
    //public static final String KEY_warn="temp";
    //public static final String KEY_pix="pix";

    private String date;
    private String time;
    private String windSpeed;
    private String tempMin;
    private String tempMax;
    private String icon;

    public Weather(String date, String time, String windSpeed, String tempMin, String tempMax, String icon) {
        this.date = date;
        this.time = time;
        this.windSpeed = windSpeed;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.icon = icon;
    }

    public Weather() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWindSpeed() {
        return "Wind "+windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getIcon() {
        String iconName = icon.replace("-","_");
        return iconName;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTemp(){
        return tempMin +"~"+ tempMax;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, Weather.class);
    }

}
