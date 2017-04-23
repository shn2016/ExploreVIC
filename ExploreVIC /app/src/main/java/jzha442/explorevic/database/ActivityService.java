package jzha442.explorevic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jzha442.explorevic.model.Activity;
import jzha442.explorevic.model.ActivityPlace;
import jzha442.explorevic.model.Filter;
import jzha442.explorevic.model.Place;
import jzha442.explorevic.utility.Utils;

import static android.R.attr.type;
import static android.provider.Contacts.SettingsColumns.KEY;
import static jzha442.explorevic.model.Activity.activityType;
import static jzha442.explorevic.model.Place.KEY_region;
import static jzha442.explorevic.utility.Utils.readParse;

/**
 * Created by Jiao on 24/03/2017.
 */

public class ActivityService {
    private DBHelper dbHelper;
    private String order = " ORDER BY "+Activity.TABLE+"."+Activity.KEY_type+", "+Activity.TABLE+"."+Activity.KEY_name;

    public ActivityService(Context context) {
        dbHelper = new DBHelper(context);
    }

    //Insert data

    public int insert(Activity a){
        //打开连接，写入数据
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Activity.KEY_ID,a.getID());
        values.put(Activity.KEY_name,a.getName());
        values.put(Activity.KEY_desc,a.getDesc());
        values.put(Activity.KEY_warn,a.getWarn());
        values.put(Activity.KEY_type,a.getType());
        values.put(Activity.KEY_pix, Utils.Bitmap2Bytes(a.getPix()));

        long a_Id=db.insert(Activity.TABLE,null,values);
        db.close();
        return (int)a_Id;
    }

    //get activities

    public ArrayList<Activity> getActivityList(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                Activity.KEY_ID+","+
                Activity.KEY_name+","+
                Activity.KEY_desc+","+
                Activity.KEY_warn+","+
                Activity.KEY_type+","+
                Activity.KEY_pix+" FROM "+Activity.TABLE+order;
        ArrayList<Activity> activityList=new ArrayList<Activity>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                Activity a=new Activity();
                a.setID(cursor.getInt(cursor.getColumnIndex(Activity.KEY_ID)));
                a.setName(cursor.getString(cursor.getColumnIndex(Activity.KEY_name)));
                a.setDesc(cursor.getString(cursor.getColumnIndex(Activity.KEY_desc)));
                a.setWarn(cursor.getString(cursor.getColumnIndex(Activity.KEY_warn)));
                a.setType(cursor.getString(cursor.getColumnIndex(Activity.KEY_type)));
                a.setPix(Utils.Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(Activity.KEY_pix))));
                activityList.add(a);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return activityList;
    }

    public Activity getActivityById(int Id){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                Activity.KEY_ID+","+
                Activity.KEY_name+","+
                Activity.KEY_desc+","+
                Activity.KEY_warn+","+
                Activity.KEY_type+","+
                Activity.KEY_pix+
                " FROM " + Activity.TABLE
                + " WHERE " +
                Activity.KEY_ID + "=?";
        Activity a=new Activity();
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(Id)});
        if(cursor.moveToFirst()){
            do{
                a.setID(cursor.getInt(cursor.getColumnIndex(Activity.KEY_ID)));
                a.setName(cursor.getString(cursor.getColumnIndex(Activity.KEY_name)));
                a.setDesc(cursor.getString(cursor.getColumnIndex(Activity.KEY_desc)));
                a.setWarn(cursor.getString(cursor.getColumnIndex(Activity.KEY_warn)));
                a.setType(cursor.getString(cursor.getColumnIndex(Activity.KEY_type)));
                a.setPix(Utils.Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(Activity.KEY_pix))));

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return a;
    }

    //filter activities
    public ArrayList<Activity> searchActivity(Filter filter){
        String keyword = filter.getWord();
        String region = filter.getRegion();
        boolean air = filter.isHasAir();
        boolean land = filter.isHasLand();
        boolean sea = filter.isHasSea();
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT DISTINCT "+
                Activity.TABLE+"."+Activity.KEY_ID+","+Activity.TABLE+"."+Activity.KEY_name+","+Activity.TABLE+"."+Activity.KEY_desc+
                ","+Activity.TABLE+"."+Activity.KEY_pix+
                " FROM "+ActivityPlace.TABLE+", "+ Place.TABLE+", "+Activity.TABLE+
                " WHERE "+ActivityPlace.TABLE+"."+ActivityPlace.KEY_activity_id+" = "+Activity.TABLE+"."+Activity.KEY_ID+
                " AND "+ActivityPlace.TABLE+"."+ActivityPlace.KEY_place_id+" = "+Place.TABLE+"."+Place.KEY_ID+" ";
        if(keyword!=null && !keyword.isEmpty()){
            selectQuery = selectQuery + "AND ("+ Activity.TABLE+"."+Activity.KEY_name+" LIKE '%"+keyword+"%' OR "+
                    ActivityPlace.TABLE+"."+ActivityPlace.KEY_name+" LIKE '%"+keyword+"%' OR "+
                    Place.TABLE+"."+Place.KEY_name+" LIKE '%"+keyword+"%') ";
        }
        if(region!=null && !region.isEmpty()){
            selectQuery = selectQuery + "AND "+Place.TABLE+"."+ Place.KEY_region+" = '"+region+"' ";
        }
        if(!air || !land || !sea) {
            String typeQuery = "";
            int iCount = 0;
            if (air) {
                typeQuery = Activity.TABLE + "." + Activity.KEY_type + " = '" + activityType[0] + "'";
                iCount++;
            }
            if (land) {
                if (iCount > 0) {
                    typeQuery += " OR ";
                }
                typeQuery += Activity.TABLE + "." + Activity.KEY_type + " = '" + activityType[1] + "'";
                iCount++;
            }
            if (sea) {
                if (iCount > 0) {
                    typeQuery += " OR ";
                }
                typeQuery += Activity.TABLE + "." + Activity.KEY_type + " = '" + activityType[2] + "'";
                iCount++;
            }
            if (iCount > 1) {
                typeQuery = "( " + typeQuery + " )";
            }
            if (iCount > 0) {
                selectQuery = selectQuery + " AND " + typeQuery;
            }
        }

        selectQuery += order;
        ArrayList<Activity> activityList=new ArrayList<Activity>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                Activity ap=new Activity();
                ap.setID(cursor.getInt(cursor.getColumnIndex(ActivityPlace.KEY_ID)));
                ap.setName(cursor.getString(cursor.getColumnIndex(ActivityPlace.KEY_name)));
                ap.setDesc(cursor.getString(cursor.getColumnIndex(ActivityPlace.KEY_desc)));
                ap.setPix(Utils.Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(ActivityPlace.KEY_pix))));
                activityList.add(ap);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return activityList;
    }

}
