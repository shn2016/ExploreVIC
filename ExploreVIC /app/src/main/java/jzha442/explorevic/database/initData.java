package jzha442.explorevic.database;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import jzha442.explorevic.model.Activity;
import jzha442.explorevic.model.ActivityPlace;
import jzha442.explorevic.model.Place;
import jzha442.explorevic.utility.Utils;

import static android.R.attr.type;
import static jzha442.explorevic.utility.Utils.readParse;

/**
 * Created by Jiao on 28/03/2017.
 */

public class initData {
    private static SQLiteDatabase db;
    private Context context;

    public initData(Context context, SQLiteDatabase db){
        this.context = context;
        this.db = db;
    }

    //get data from json file
    public String getJsonFile(String filename){
        String result = "";
        try {
            InputStreamReader isr = new InputStreamReader(context.getAssets().open(filename),"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while((line = br.readLine()) != null){
                builder.append(line);
            }
            br.close();
            isr.close();
            //JSONObject testjson = new JSONObject(builder.toString());//builder读取了JSON中的数据。
            //直接传入JSONObject来构造一个实例
            result = builder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Bitmap getAssetImage(String image){
        InputStream open = null;
        AssetManager am = context.getAssets();
        try {
            open = am.open("pix/"+image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bmp = BitmapFactory.decodeStream(open);
        return bmp;
    }

    //Load Activity Data

    public void loadActivity(String filename) throws Exception {
        ArrayList<Activity> list = getActivityFromJson(getJsonFile(filename));
        addActivityToDatabase(list);
    }

    private ArrayList<Activity> getActivityFromJson(String data) throws Exception {

        ArrayList<Activity> mlists = new ArrayList<Activity>();

        JSONObject activities = new JSONObject(data);

        JSONArray array = activities.getJSONArray("activity");

        for (int i = 0; i < array.length(); i++) {
            JSONObject item = array.getJSONObject(i);
            int ID = item.getInt("id");
            String name = item.getString("activity_name");
            String desc = item.getString("description");
            String warn = item.getString("warning");
            String type = item.getString("type");
            String pixname = item.getString("pix");
            Bitmap pix = null;

            if(pixname!=null && !pixname.isEmpty()){
                pix = getAssetImage("a/"+pixname);
            }
            Activity a = new Activity(ID, name, desc, warn, type, pix);
            mlists.add(a);
        }

        return mlists;
    }

    private static void addActivityToDatabase(ArrayList<Activity> as) {
        //SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "insert into "+Activity.TABLE+
                "("+Activity.KEY_ID+","+Activity.KEY_name+","+Activity.KEY_desc+
                ","+Activity.KEY_warn+","+Activity.KEY_type+","+Activity.KEY_pix
                +") values(?,?,?,?,?,?)";
        SQLiteStatement stat = db.compileStatement(sql);
        db.beginTransaction();
        for (Activity a : as) {
            stat.bindLong(1, a.getID());
            stat.bindString(2, a.getName());
            stat.bindString(3, a.getDesc());
            stat.bindString(4, a.getWarn());
            stat.bindString(5, a.getType());
            stat.bindBlob(6, Utils.Bitmap2Bytes(a.getPix()));
            stat.executeInsert();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        //db.close();
    }

    //Load Place Data

    public void loadPlace(String filename) throws Exception {
        ArrayList<Place> list = getPlaceFromJson(getJsonFile(filename));
        addPlaceToDatabase(list);
    }

    private ArrayList<Place> getPlaceFromJson(String data) throws Exception {

        ArrayList<Place> mlists = new ArrayList<Place>();

        JSONObject activities = new JSONObject(data);

        JSONArray array = activities.getJSONArray("place");

        for (int i = 0; i < array.length(); i++) {
            JSONObject item = array.getJSONObject(i);
            int ID = item.getInt("id");
            String name = item.getString("place_name");
            String desc = item.getString("description");
            String faci = item.getString("facility");
            Double lati = item.getDouble("latitude");
            Double longi = item.getDouble("longitude");
            String region = item.getString("region");
            String suburb = item.getString("suburb");
            String street = item.getString("street");
            String post = item.getString("postcode");
            String open_time = item.getString("open_time");
            String close_time = item.getString("close_time");
            int weekday = item.getInt("weekday");
            int weekend = item.getInt("weekend");
            int holiday = item.getInt("holiday");
            String website = item.getString("website_link");
            String pixname = item.getString("pix");
            Bitmap pix = null;

            if(pixname!=null && !pixname.isEmpty()){
                pix = getAssetImage("p/"+pixname);
            }

            Place p = new Place(ID, name, desc, faci, lati, longi, region, suburb, street, post, open_time, close_time, weekday, weekend, holiday, website, pix);

            mlists.add(p);
        }

        return mlists;
    }

    private static void addPlaceToDatabase(ArrayList<Place> ps) throws Exception{
        String sql = "insert into "+Place.TABLE+
                "("+Place.KEY_ID+","+Place.KEY_name+","+Place.KEY_desc+
                ","+Place.KEY_facility+","+Place.KEY_latitude+","+Place.KEY_longitude+
                ","+Place.KEY_region+","+Place.KEY_suburb+","+Place.KEY_street+
                ","+Place.KEY_postcode+","+Place.KEY_open_time+","+Place.KEY_close_time+
                ","+Place.KEY_weekday+","+Place.KEY_weekend+","+Place.KEY_holiday+
                ","+Place.KEY_website+","+Place.KEY_pix+
                ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteStatement stat = db.compileStatement(sql);
        db.beginTransaction();
        for (Place p : ps) {
            stat.bindLong(1, p.getID());
            stat.bindString(2, p.getName());
            stat.bindString(3, p.getDesc());
            stat.bindString(4, p.getFacility());
            stat.bindDouble(5, p.getLatitude());
            stat.bindDouble(6, p.getLongitude());
            stat.bindString(7, p.getRegion());
            stat.bindString(8, p.getSuburb());
            stat.bindString(9, p.getStreet());
            stat.bindString(10, p.getPostcode());
            stat.bindLong(11, p.getOpen_time());
            stat.bindLong(12, p.getClose_time());
            stat.bindLong(13, p.bool2int(p.isWeekday()));
            stat.bindLong(14, p.bool2int(p.isWeekend()));
            stat.bindLong(15, p.bool2int(p.isHoliday()));
            stat.bindString(16, p.getWebsite());
            stat.bindBlob(17, Utils.Bitmap2Bytes(p.getPix()));
            stat.executeInsert();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    //Load Activity_Places Data

    public void loadActivityPlaces(String filename) throws Exception {
        ArrayList<ActivityPlace> list = getActivityPlaceFromJson(getJsonFile(filename));
        addActivityPlaceToDatabase(list);
    }

    private ArrayList<ActivityPlace> getActivityPlaceFromJson(String data) throws Exception {

        ArrayList<ActivityPlace> mlists = new ArrayList<ActivityPlace>();

        JSONObject activities = new JSONObject(data);

        JSONArray array = activities.getJSONArray("activity_places");

        for (int i = 0; i < array.length(); i++) {
            JSONObject item = array.getJSONObject(i);
            int ID = item.getInt("id");
            String name = item.getString("name");
            String desc = item.getString("description");
            int min_cost = item.getInt("minimum_cost");
            int max_cost = item.getInt("maximum_cost");
            int a_id = item.getInt("activity_id");
            int p_id = item.getInt("place_id");

            String pixname = item.getString("pix");
            Bitmap pix = null;

            if(pixname!=null && !pixname.isEmpty()){
                pix = getAssetImage("ap/"+pixname);
            }

            ActivityPlace p = new ActivityPlace(ID, name, desc, pix, a_id, p_id);

            mlists.add(p);
        }

        return mlists;
    }

    private static void addActivityPlaceToDatabase(ArrayList<ActivityPlace> aps) {
        String sql = "insert into "+ActivityPlace.TABLE+
                "("+ActivityPlace.KEY_ID+","+ActivityPlace.KEY_name+","+ActivityPlace.KEY_desc+","+ActivityPlace.KEY_min_cost+","+ActivityPlace.KEY_max_cost+
                ","+ActivityPlace.KEY_pix+","+ActivityPlace.KEY_activity_id+","+ActivityPlace.KEY_place_id
                +") values(?,?,?,?,?,?,?,?)";
        SQLiteStatement stat = db.compileStatement(sql);
        db.beginTransaction();
        for (ActivityPlace ap : aps) {
            stat.bindLong(1, ap.getID());
            stat.bindString(2, ap.getName());
            stat.bindString(3, ap.getDesc());
            stat.bindLong(4, ap.getMin_cost());
            stat.bindLong(5, ap.getMax_cost());
            stat.bindBlob(6, Utils.Bitmap2Bytes(ap.getPix()));
            stat.bindLong(7, ap.getActivity_id());
            stat.bindLong(8, ap.getPlace_id());
            stat.executeInsert();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }
}
