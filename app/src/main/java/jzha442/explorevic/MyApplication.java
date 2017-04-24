package jzha442.explorevic;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jzha442.explorevic.database.FavouritePlaceService;
import jzha442.explorevic.database.PlaceService;
import jzha442.explorevic.model.FavouritePlace;
import jzha442.explorevic.model.Place;
import jzha442.explorevic.model.PlaceWeather;
import jzha442.explorevic.model.Weather;
import jzha442.explorevic.utility.Utils;
import jzha442.explorevic.webservice.DownloadWeather;

/**
 * Created by Jiao on 30/03/2017.
 */

public class MyApplication extends Application {
    public static Typeface CustomTypeFace;
    public static ArrayList<PlaceWeather> weathers = new ArrayList<PlaceWeather>();
    public static ArrayList<PlaceWeather> customWeathers = new ArrayList<PlaceWeather>();
    public static Date getWeatherDate;

    @Override
    public void onCreate() {
        super.onCreate();
        // 加载自定义字体
        CustomTypeFace = Typeface.createFromAsset(getAssets(), "fonts/KGMissKindyChunky.ttf");

        try
        {
            Field field = Typeface.class.getDeclaredField("SERIF");
            field.setAccessible(true);
            field.set(null, CustomTypeFace);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        final Context context = getApplicationContext();
        //getWeather data for place
        if(Utils.hasWeatherCache(context, "place")){
            try {
                weathers = Utils.getWeatherFromFile(context, "place");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            if (Utils.isConnectedNetwork(this)) {
                PlaceService ps = new PlaceService(this);
                final ArrayList<Place> placeList = ps.getPlaceList();
                for (Place p : placeList) {
                    DownloadWeather.placeIdTask asyncTask = new DownloadWeather.placeIdTask(new DownloadWeather.AsyncResponse() {
                        public void processFinish(PlaceWeather pw) {
                            weathers.add(pw);
                            if (weathers.size() == placeList.size()) {
                                Utils.storeWeatherToFile(context, weathers, "place");
                            }
                        }
                    });
                    asyncTask.execute(p.getLatitude() + "", p.getLongitude() + "", p.getID() + ""); //  asyncTask.execute("Latitude", "Longitude"
                }

            } else {
                Utils.showMessage(this, "Please connect to the network for more services!");
            }
        }

        //getWeather data for customised favourite place
        FavouritePlaceService fps = new FavouritePlaceService(this);
        final ArrayList<FavouritePlace> fpList = fps.getLatLngList();
        if(fpList.size()>0 && Utils.hasWeatherCache(context, "custom")){
            try {
                customWeathers = Utils.getWeatherFromFile(context, "custom");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            if (Utils.isConnectedNetwork(this)) {
                for (FavouritePlace fp : fpList) {
                    DownloadWeather.placeIdTask asyncTask = new DownloadWeather.placeIdTask(new DownloadWeather.AsyncResponse() {
                        public void processFinish(PlaceWeather pw) {
                            customWeathers.add(pw);
                            if (customWeathers.size() == fpList.size()) {
                                Utils.storeWeatherToFile(context, customWeathers, "custom");
                            }
                        }
                    });
                    asyncTask.execute(fp.getLatitude() + "", fp.getLongitude() + "", fp.getID() + ""); //  asyncTask.execute("Latitude", "Longitude"
                }

            }
        }
    }



}
