package jzha442.explorevic.database;

import java.util.ArrayList;

import jzha442.explorevic.MyApplication;
import jzha442.explorevic.model.PlaceWeather;
import jzha442.explorevic.model.Weather;

/**
 * Created by Jiao on 02/04/2017.
 */

public class PlaceWeatherService {

    private ArrayList<PlaceWeather> pwList;

    //non sql database - temporary dataset
    public PlaceWeatherService(){
        pwList=MyApplication.weathers;
    }

    public ArrayList<Weather> getWeatherList(int placeID){
        if(pwList.size()>0){
            for(PlaceWeather pw: pwList){
                if(pw.getPlaceID() == placeID){
                    return pw.getWeatherList();
                }
            }
        }
        return null;
    }

}
