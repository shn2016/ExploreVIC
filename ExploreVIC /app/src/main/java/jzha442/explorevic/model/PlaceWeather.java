package jzha442.explorevic.model;

import java.util.ArrayList;

/**
 * Created by Jiao on 02/04/2017.
 */

public class PlaceWeather {
    private int placeID;
    private ArrayList<Weather> weatherList = new ArrayList<Weather>();

    public PlaceWeather() {
    }

    public PlaceWeather(int placeID, ArrayList<Weather> weatherList) {
        this.placeID = placeID;
        this.weatherList = weatherList;
    }

    public int getPlaceID() {
        return placeID;
    }

    public void setPlaceID(int placeID) {
        this.placeID = placeID;
    }

    public ArrayList<Weather> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(ArrayList<Weather> weatherList) {
        this.weatherList = weatherList;
    }
}
