package jzha442.explorevic.webservice;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jzha442.explorevic.model.PlaceWeather;
import jzha442.explorevic.model.Weather;
import jzha442.explorevic.utility.Timeconversion;

/**
 * Created by Kai Gao on 17/3/30.
 */

public class DownloadWeather{

    private static final String OPEN_WEATHER_MAP_URL =
            "https://api.darksky.net/forecast";

    private static final String OPEN_WEATHER_MAP_API = "eb881196ab5b6a8cbbcbe567b2b0580c";//eb881196ab5b6a8cbbcbe567b2b0580c  //e08537b1f13f3f07ed4a9535e3e531eb

    public interface AsyncResponse{

        void processFinish(PlaceWeather pw);
    }
    public static class placeIdTask extends AsyncTask<String, Void, JSONObject> {

        public AsyncResponse delegate = null;//Call back interface
        private int placeID;

        public placeIdTask(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interface through constructor
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            JSONObject jsonWeather = null;
            try {
                jsonWeather = getWeatherJSON(params[0], params[1]);
                placeID = Integer.parseInt(params[2]);
            } catch (Exception e) {
                Log.d("Error", "Cannot process JSON results", e);
            }

            return jsonWeather;
        }

        // processing json to the way we want
        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if(json != null){
                    //JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    //JSONObject main = json.getJSONObject("main");
                    //JSONObject wind = json.getJSONObject("wind");

                    //String icon = details.getString("icon");

                    /*String id = details.getString("id");
                    String icon = details.getString("icon")+".png";
                    String weatherType = details.getString("description").toUpperCase();
                    String temperature = String.format("%.2f", main.getDouble("temp"))+ "°";
                    String windSpeed = wind.getString("speed") + "m/s";*/
                    ArrayList<Weather> ws = transferJsonToWeather(json);
                    PlaceWeather pw = new PlaceWeather(placeID, ws);
                    delegate.processFinish(pw);

                }
            } catch (Exception e) {
                //Log.e(LOG_TAG, "Cannot process JSON results", e);
            }

        }
    }

     // get json first.
    public static JSONObject getWeatherJSON(String lat, String lon){
        try {
            String weatherUrl = OPEN_WEATHER_MAP_URL +"/"+OPEN_WEATHER_MAP_API+"/"+lat+","+lon;
            URL url = new URL(String.format(weatherUrl));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            //connection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
          /*  if(data.getInt("cod") != 200){
                return null;
            }*/

            return data;
        }catch(Exception e){
            return null;
        }
    }

    public static ArrayList<Weather> transferJsonToWeather(JSONObject json){
        ArrayList<Weather> ws = new ArrayList<>();
        try {
            JSONArray dailyData = json.getJSONObject("daily").getJSONArray("data");
            //JSONObject main = json.getJSONObject("main");
            //JSONObject wind = json.getJSONObject("wind");

            for(int i=0;i<5; i++) {
                Timeconversion t = new Timeconversion();
                JSONObject dailyWeather = dailyData.getJSONObject(i);

                String icon = dailyWeather.getString("icon");
                String time = t.unix2time(new Long(dailyWeather.getInt("temperatureMinTime")));
                String day = t.unix2week(new Long(dailyWeather.getInt("temperatureMinTime")))+".";
                String tempMin = String.format("%.2f",timeTransfer(dailyWeather.getDouble("temperatureMin")))+"°C";
                String tempMax = String.format("%.2f",timeTransfer(dailyWeather.getDouble("temperatureMax")))+"°C";
                String windSpeed = String.format("%.2f",kmTransfer(dailyWeather.getDouble("windSpeed")))+" km/h";

                Weather w = new Weather(day, time, windSpeed, tempMin, tempMax, icon);
                ws.add(w);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            return ws;
        }
    }

    public static Double timeTransfer(Double tempF) {
        Double tempC =  (tempF - 32) * 5 / 9;
        return tempC;
    }

    public static Double kmTransfer(Double miles){
        Double km = miles *1.609344;
        return km;
    }

}
