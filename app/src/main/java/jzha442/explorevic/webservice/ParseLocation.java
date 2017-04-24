package jzha442.explorevic.webservice;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import jzha442.explorevic.model.NearbyPlace;

/**
 * Created by kaigao on 17/4/22.
 */

public class ParseLocation {

    private static final String TAG = LocationSearchService.class.getSimpleName();

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/geocode";
    private static final String OUT_JSON = "/json?";
    private static final String API_KEY = "AIzaSyALwOksgsH8TwUS0DTcy6uwdPrlzq2Au08";

    public interface AsyncResponse{

        void processFinish(LatLng np);
    }


    public static class placeIdTask extends AsyncTask<String, Void, LatLng> {

        public ParseLocation.AsyncResponse delegate = null;//Call back interface
        private String keyword;

        public placeIdTask(ParseLocation.AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interface through constructor
        }
        protected LatLng doInBackground(String... params) {

            LatLng rawData = null;
            try {
                rawData = getLatLng(params[0]);
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

                    delegate.processFinish(json);
                    Log.i("return cor:", json+"");

                }
            } catch (Exception e) {
                //Log.e(LOG_TAG, "Cannot process JSON results", e);
            }

        }
    }

    public static LatLng getLatLng(String input) {
        LatLng result = null;
        NearbyPlace placeMap = null;

        String mAddress = input.trim();
        mAddress = mAddress.replaceAll(" ","+");
        Log.i("mdreass",mAddress);
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + OUT_JSON);
            sb.append("address="+mAddress);
            sb.append("&key=" + API_KEY);

            URL url = new URL(sb.toString());
            Log.i("url",url+"");
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error processing Places API URL", e);
            return result;
        } catch (IOException e) {
            Log.e(TAG, "Error connecting to Places API", e);
            return result;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray jsonArray = jsonObj.getJSONArray("results");
            JSONObject json = (JSONObject) jsonArray.get(0);

            String latitude = json.getJSONObject("geometry").getJSONObject("location").getString("lat");
            String longitude = json.getJSONObject("geometry").getJSONObject("location").getString("lng");

            double lat = Double.parseDouble(latitude);
            double lon = Double.parseDouble(longitude);
            result = new LatLng(lat,lon);

            Log.i("lat/lon", lat+""+lon);

        } catch (JSONException e) {
            Log.e(TAG, "Cannot process JSON results", e);
        }

        return result;
    }


}
