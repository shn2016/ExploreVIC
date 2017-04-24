package jzha442.explorevic.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.List;

import jzha442.explorevic.model.Activity;
import jzha442.explorevic.model.ActivityPlace;
import jzha442.explorevic.model.Place;
import jzha442.explorevic.utility.Utils;

import static jzha442.explorevic.model.Place.KEY_region;

/**
 * Created by Jiao on 26/03/2017.
 */

public class ActivityPlaceService {
    private DBHelper dbHelper;

    public ActivityPlaceService(Context context) {
        dbHelper = new DBHelper(context);
    }

    //get all activity_places
    public ArrayList<ActivityPlace> getActivityPlaceList(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                ActivityPlace.KEY_ID+","+ActivityPlace.KEY_name+","+ActivityPlace.KEY_desc+
                ","+ActivityPlace.KEY_pix+","+ActivityPlace.KEY_activity_id+","+ActivityPlace.KEY_place_id+
                " FROM "+ActivityPlace.TABLE;
        ArrayList<ActivityPlace> activityPlaceList=new ArrayList<ActivityPlace>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                ActivityPlace ap=new ActivityPlace();
                ap.setID(cursor.getInt(cursor.getColumnIndex(ActivityPlace.KEY_ID)));
                ap.setName(cursor.getString(cursor.getColumnIndex(ActivityPlace.KEY_name)));
                ap.setDesc(cursor.getString(cursor.getColumnIndex(ActivityPlace.KEY_desc)));
                ap.setPix(Utils.Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(ActivityPlace.KEY_pix))));
                ap.setActivity_id(cursor.getInt(cursor.getColumnIndex(ActivityPlace.KEY_activity_id)));
                ap.setPlace_id(cursor.getInt(cursor.getColumnIndex(ActivityPlace.KEY_place_id)));
                activityPlaceList.add(ap);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return activityPlaceList;
    }

    //get all different facilities for places
    public ArrayList<String> getAvailableActivityList(int pID){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT DISTINCT "+Activity.TABLE+"."+Activity.KEY_name+
                " FROM "+ActivityPlace.TABLE+", "+ Place.TABLE+", "+Activity.TABLE+
                " WHERE "+ActivityPlace.TABLE+"."+ActivityPlace.KEY_activity_id+" = "+Activity.TABLE+"."+Activity.KEY_ID+
                " AND "+ActivityPlace.TABLE+"."+ActivityPlace.KEY_place_id+" = "+Place.TABLE+"."+Place.KEY_ID+
                " AND "+Place.TABLE+"."+Place.KEY_ID+" = "+pID+
                " ORDER BY "+Activity.TABLE+"."+Activity.KEY_name;
        ArrayList<String> actiList=new ArrayList<String>();

        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                String activity = cursor.getString(cursor.getColumnIndex(Activity.TABLE+"."+Activity.KEY_name));
                activity = activity.trim();
                if(!actiList.contains(activity)){
                    actiList.add(activity);
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return actiList;
    }

    //get activity_place by id
    public ActivityPlace getActivityPlaceById(int Id){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                ActivityPlace.KEY_ID+","+ActivityPlace.KEY_name+","+ActivityPlace.KEY_desc+
                ","+ActivityPlace.KEY_pix+","+ActivityPlace.KEY_activity_id+","+ActivityPlace.KEY_place_id+
                " FROM " + ActivityPlace.TABLE
                + " WHERE " +
                ActivityPlace.KEY_ID + "=?";
        ActivityPlace ap=new ActivityPlace();
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(Id)});
        if(cursor.moveToFirst()){
            do{
                ap.setID(cursor.getInt(cursor.getColumnIndex(ActivityPlace.KEY_ID)));
                ap.setName(cursor.getString(cursor.getColumnIndex(ActivityPlace.KEY_name)));
                ap.setDesc(cursor.getString(cursor.getColumnIndex(ActivityPlace.KEY_desc)));
                ap.setPix(Utils.Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(ActivityPlace.KEY_pix))));
                ap.setActivity_id(cursor.getInt(cursor.getColumnIndex(ActivityPlace.KEY_activity_id)));
                ap.setPlace_id(cursor.getInt(cursor.getColumnIndex(ActivityPlace.KEY_place_id)));

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return ap;
    }

    //get one activity_place by activity id and place id
    public ActivityPlace getActivityPlaceById(int aId, int pId){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                ActivityPlace.KEY_ID+","+ActivityPlace.KEY_name+","+ActivityPlace.KEY_desc+
                ","+ActivityPlace.KEY_pix+","+ActivityPlace.KEY_activity_id+","+ActivityPlace.KEY_place_id+
                " FROM " + ActivityPlace.TABLE+
                " WHERE " + ActivityPlace.KEY_activity_id + " = " + aId +
                " AND " + ActivityPlace.KEY_place_id + " = " + pId;
        ActivityPlace ap=new ActivityPlace();
        Cursor cursor=db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                ap.setID(cursor.getInt(cursor.getColumnIndex(ActivityPlace.KEY_ID)));
                ap.setName(cursor.getString(cursor.getColumnIndex(ActivityPlace.KEY_name)));
                ap.setDesc(cursor.getString(cursor.getColumnIndex(ActivityPlace.KEY_desc)));
                ap.setPix(Utils.Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(ActivityPlace.KEY_pix))));
                ap.setActivity_id(cursor.getInt(cursor.getColumnIndex(ActivityPlace.KEY_activity_id)));
                ap.setPlace_id(cursor.getInt(cursor.getColumnIndex(ActivityPlace.KEY_place_id)));

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return ap;
    }
}
