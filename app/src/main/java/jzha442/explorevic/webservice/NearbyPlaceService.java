package jzha442.explorevic.webservice;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jzha442.explorevic.model.NearbyPlace;

/**
 * Created by kaigao on 17/4/20.
 */

public class NearbyPlaceService {

    private static final String MAP_URL ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

    private static final String MAP_API = "AIzaSyALwOksgsH8TwUS0DTcy6uwdPrlzq2Au08";

    public interface AsyncResponse{

        void processFinish(List<NearbyPlace> np);
    }

    public static class placeIdTask extends AsyncTask<String, Void, String> {

        public NearbyPlaceService.AsyncResponse delegate = null;//Call back interface
        private String keyword;

        public placeIdTask(NearbyPlaceService.AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interface through constructor
        }

        @Override
        protected String doInBackground(String... params) {

            String rawData = "";
            try {
                rawData = getNearbyPlaceData(params[0], params[1],params[2]);
            } catch (Exception e) {
                Log.d("Error", "Cannot process JSON results", e);
            }

            return rawData;
        }

        // processing json to the way we want
        @Override
        protected void onPostExecute(String json) {
            try {
                if(json != null){

                    List<NearbyPlace> np = getNearbyPlaceList(json);
                    delegate.processFinish(np);

                }
            } catch (Exception e) {
                //Log.e(LOG_TAG, "Cannot process JSON results", e);
            }

        }
    }

    public static String getNearbyPlaceData(String lat, String lon, String w) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        String strUrl = MAP_URL+"location="+lat+","+lon+"&radius=1500&" + w+"&key=" +MAP_API;
        Log.i("url",strUrl);
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public static List<NearbyPlace> getNearbyPlaceList(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            Log.d("Places", "parse");
            jsonObject = new JSONObject((String) jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            Log.d("Places", "parse error");
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    private static List<NearbyPlace> getPlaces(JSONArray jsonArray) {
        int placesCount = jsonArray.length();
        List<NearbyPlace> placesList = new ArrayList<>();
        NearbyPlace placeMap = null;
        Log.d("Places", "getPlaces");

        for (int i = 0; i < placesCount; i++) {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
                Log.d("Places", "Adding places");

            } catch (JSONException e) {
                Log.d("Places", "Error in Adding places");
                e.printStackTrace();
            }
        }
        return placesList;
    }

    private static NearbyPlace getPlace(JSONObject googlePlaceJson) {
        NearbyPlace googlePlaceMap = new NearbyPlace();
        String placeName = "unknown";
        String vicinity = "unknown";
        String latitude = "";
        String longitude = "";
        String reference = "";

        Log.d("getPlace", "Entered");

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googlePlaceJson.getString("reference");
            googlePlaceMap.setPlaceName(placeName);
            googlePlaceMap.setVicinity(vicinity);
            googlePlaceMap.setLatitude(latitude);
            googlePlaceMap.setLongitude(longitude);
            googlePlaceMap.setReference(reference);
            Log.d("getPlace", "Putting Places");
        } catch (JSONException e) {
            Log.d("getPlace", "Error");
            e.printStackTrace();
        }
        return googlePlaceMap;
    }
}
