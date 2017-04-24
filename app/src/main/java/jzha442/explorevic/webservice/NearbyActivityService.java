package jzha442.explorevic.webservice;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jzha442.explorevic.database.ActivityPlaceService;
import jzha442.explorevic.database.ActivityService;
import jzha442.explorevic.database.PlaceService;
import jzha442.explorevic.model.ActivityPlace;
import jzha442.explorevic.model.NearbyPlace;
import jzha442.explorevic.model.Place;

import static jzha442.explorevic.webservice.NearbyPlaceService.getNearbyPlaceData;
import static jzha442.explorevic.webservice.NearbyPlaceService.getNearbyPlaceList;

/**
 * Created by kaigao on 17/4/18.
 */

public class NearbyActivityService {
    private static double EARTH_RADIUS = 6378.137;

    private static Context context;

    public NearbyActivityService(Context context){
        this.context = context;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }
    public interface AsyncResponse{

        void processFinish(List<HashMap<String, String>> np);
    }

    public static class placeIdTask extends AsyncTask<String, Void, LatLng> {
        public NearbyActivityService.AsyncResponse delegate = null;//Call back interface
        private String keyword;

        public placeIdTask(NearbyActivityService.AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interface through constructor
        }


        @Override
        protected LatLng doInBackground(String... params) {

            LatLng rawData =new LatLng(0.0,0.0);
            try {
                double lat = Double.parseDouble(params[0]);
                double lon = Double.parseDouble(params[1]);
                rawData = new LatLng(lat,lon);
            } catch (Exception e) {
                Log.d("Error", "Cannot process JSON results", e);
            }

            return rawData;
        }

        // processing json to the way we want
        @Override
        protected void onPostExecute(LatLng json) {
            try {
                if(json != null){

                    double lat = json.latitude;
                    double lon=json.longitude;
                    List<HashMap<String, String>> np = getActivityPlace(lat,lon);
                    delegate.processFinish(np);

                }
            } catch (Exception e) {
                //Log.e(LOG_TAG, "Cannot process JSON results", e);
            }

        }
    }


        public static List<HashMap<String, String>> getActivityPlace(double lat1, double lon1){
            ActivityPlaceService a = new ActivityPlaceService(context);
        List<HashMap<String, String>> l = new ArrayList<>();
        ArrayList<ActivityPlace> lap = a.getActivityPlaceList();
        for(ActivityPlace ap: lap){
            HashMap<String, String> ll =  new HashMap<>();
            int pid = ap.getPlace_id();
            int aid = ap.getID();
            PlaceService ps = new PlaceService(context);
            Place p  = ps.getPlaceById(pid);
            Double lat2 = p.getLatitude();
            Double lon2 = p.getLongitude();
            String address = p.getAddress();
            if( getDistance(lat1,lon1,lat2,lon2) < 5000){
                String lat = String.format("lat2:%.3f",lat2);
                String lon = String.format("lon2:%.3f",lon2);
                String p_id= Integer.toString(pid);
                String a_id= Integer.toString(aid);

                ll.put("name",ap.getName());
                ll.put("place_id",p_id);
                ll.put("activity_id",a_id);
                ll.put("latitude",lat);
                ll.put("longitude",lon);
                ll.put("vicinity",address);

                l.add(ll);
            }
        }
        return l;

    }
    public static double getDistance(double lat1, double lng1, double lat2,
                                     double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s*1000;
        return s;
    }
}
