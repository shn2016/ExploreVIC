package jzha442.explorevic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import jzha442.explorevic.model.Activity;
import jzha442.explorevic.model.ActivityPlace;
import jzha442.explorevic.model.Place;
import jzha442.explorevic.model.SearchRecord;

import static jzha442.explorevic.R.id.imageView;
import static jzha442.explorevic.model.Activity.KEY_ID;

/**
 * Created by Jiao on 24/03/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    //Database version number
    private static final int DATABASE_VERSION=1;

    //Database Name
    private static final String DATABASE_NAME="exvic.db";

    private Context context;

    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }

    //是在数据库每一次被创建的时候调用的
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create tables
        //--Create Activity table
        String CREATE_TABLE_Activity="CREATE TABLE "+ Activity.TABLE+"("
                +Activity.KEY_ID+" INTEGER PRIMARY KEY, "//AUTOINCREMENT
                +Activity.KEY_name+" TEXT, "
                +Activity.KEY_desc+" TEXT, "
                +Activity.KEY_warn+" TEXT, "
                +Activity.KEY_type+" TEXT, "
                +Activity.KEY_pix+" BLOB)";
        db.execSQL(CREATE_TABLE_Activity);
        //--Create Place table
        String CREATE_TABLE_Place="CREATE TABLE "+ Place.TABLE+"("
                +Place.KEY_ID+" INTEGER PRIMARY KEY, "//AUTOINCREMENT
                +Place.KEY_name+" TEXT, "
                +Place.KEY_desc+" TEXT, "
                +Place.KEY_facility+" TEXT, "
                +Place.KEY_latitude+" REAL, "
                +Place.KEY_longitude+" REAL, "
                +Place.KEY_region+" TEXT, "
                +Place.KEY_suburb+" TEXT, "
                +Place.KEY_street+" TEXT, "
                +Place.KEY_postcode+" TEXT, "
                +Place.KEY_open_time+" INTEGER, "
                +Place.KEY_close_time+" INTEGER, "
                //0 is FALSE 1 is TRUE
                +Place.KEY_weekday+" INTEGER DEFAULT 1, "
                +Place.KEY_weekend+" INTEGER DEFAULT 1, "
                +Place.KEY_holiday+" INTEGER DEFAULT 1, "
                +Place.KEY_website+" TEXT, "
                +Place.KEY_pix+" BLOB)";
        db.execSQL(CREATE_TABLE_Place);
        //--Create ActivityPlace table
        String CREATE_TABLE_ActivityPlace="CREATE TABLE "+ ActivityPlace.TABLE+"("
                +ActivityPlace.KEY_ID+" INTEGER PRIMARY KEY, "//AUTOINCREMENT
                +ActivityPlace.KEY_name+" TEXT, "
                +ActivityPlace.KEY_desc+" TEXT, "
                +ActivityPlace.KEY_min_cost+" INTEGER, "
                +ActivityPlace.KEY_max_cost+" INTEGER, "
                +ActivityPlace.KEY_pix+" BLOB, "
                +ActivityPlace.KEY_activity_id+" INTEGER, "
                +ActivityPlace.KEY_place_id+" INTEGER, "
                +"FOREIGN KEY ("+ActivityPlace.KEY_activity_id+") REFERENCES "+Activity.TABLE+"("+Activity.KEY_ID+" ), "
                +"FOREIGN KEY ("+ActivityPlace.KEY_place_id+") REFERENCES "+Place.TABLE+"("+Place.KEY_ID+") )";
        db.execSQL(CREATE_TABLE_ActivityPlace);
        //--Create SearchRecord table
        String CREATE_TABLE_SearchRecord="CREATE TABLE "+ SearchRecord.TABLE+"("
                +SearchRecord.KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "//AUTOINCREMENT
                +SearchRecord.KEY_word+" TEXT)";
        db.execSQL(CREATE_TABLE_SearchRecord);

        //load activity data
        initData init = new initData(context, db);
        try {
            init.loadActivity("Activity.json");
            init.loadPlace("Place.json");
            init.loadActivityPlaces("ActivityPlace.json");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //在数据库的版本发生变化时会被调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //如果旧表存在，删除，所以数据将会消失
        db.execSQL("DROP TABLE IF EXISTS "+ Activity.TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ Place.TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ ActivityPlace.TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ SearchRecord.TABLE);

        //再次创建表
        onCreate(db);
    }
}
