package jzha442.explorevic.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import jzha442.explorevic.model.Activity;
import jzha442.explorevic.model.ActivityPlace;
import jzha442.explorevic.model.Filter;
import jzha442.explorevic.model.Place;
import jzha442.explorevic.utility.Utils;

import static android.R.attr.filter;
import static jzha442.explorevic.model.Activity.activityType;

/**
 * Created by Jiao on 24/03/2017.
 */

public class PlaceService {
    private DBHelper dbHelper;

    public PlaceService(Context context) {
        dbHelper = new DBHelper(context);
    }

    //get all places
    public ArrayList<Place> getPlaceList(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                Place.KEY_ID+","+Place.KEY_name+","+Place.KEY_desc+
                ","+Place.KEY_facility+","+Place.KEY_latitude+","+Place.KEY_longitude+
                ","+Place.KEY_region+","+Place.KEY_suburb+","+Place.KEY_street+
                ","+Place.KEY_postcode+","+Place.KEY_open_time+","+Place.KEY_close_time+
                ","+Place.KEY_weekday+","+Place.KEY_weekend+","+Place.KEY_holiday+
                ","+Place.KEY_website+","+Place.KEY_pix+
                " FROM "+Place.TABLE;
        ArrayList<Place> placeList=new ArrayList<Place>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                Place p=new Place();
                p.setID(cursor.getInt(cursor.getColumnIndex(Place.KEY_ID)));
                p.setName(cursor.getString(cursor.getColumnIndex(Place.KEY_name)));
                p.setDesc(cursor.getString(cursor.getColumnIndex(Place.KEY_desc)));
                p.setFacility(cursor.getString(cursor.getColumnIndex(Place.KEY_facility)));
                p.setLatitude(cursor.getDouble(cursor.getColumnIndex(Place.KEY_latitude)));
                p.setLongitude(cursor.getDouble(cursor.getColumnIndex(Place.KEY_longitude)));
                p.setRegion(cursor.getString(cursor.getColumnIndex(Place.KEY_region)));
                p.setSuburb(cursor.getString(cursor.getColumnIndex(Place.KEY_suburb)));
                p.setStreet(cursor.getString(cursor.getColumnIndex(Place.KEY_street)));
                p.setPostcode(cursor.getString(cursor.getColumnIndex(Place.KEY_postcode)));
                p.setOpen_time(cursor.getInt(cursor.getColumnIndex(Place.KEY_open_time)));
                p.setClose_time(cursor.getInt(cursor.getColumnIndex(Place.KEY_close_time)));
                p.setWeekday(p.int2bool(cursor.getInt(cursor.getColumnIndex(Place.KEY_weekday))));
                p.setWeekend(p.int2bool(cursor.getInt(cursor.getColumnIndex(Place.KEY_weekend))));
                p.setHoliday(p.int2bool(cursor.getInt(cursor.getColumnIndex(Place.KEY_holiday))));
                p.setWebsite(cursor.getString(cursor.getColumnIndex(Place.KEY_website)));
                p.setPix(Utils.Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(Place.KEY_pix))));
                placeList.add(p);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return placeList;
    }

    //get one place by id
    public Place getPlaceById(int Id){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                Place.KEY_ID+","+Place.KEY_name+","+Place.KEY_desc+
                ","+Place.KEY_facility+","+Place.KEY_latitude+","+Place.KEY_longitude+
                ","+Place.KEY_region+","+Place.KEY_suburb+","+Place.KEY_street+
                ","+Place.KEY_postcode+","+Place.KEY_open_time+","+Place.KEY_close_time+
                ","+Place.KEY_weekday+","+Place.KEY_weekend+","+Place.KEY_holiday+
                ","+Place.KEY_website+","+Place.KEY_pix+
                " FROM " + Place.TABLE
                + " WHERE " +
                Place.KEY_ID + "=?";
        Place p=new Place();
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(Id)});
        if(cursor.moveToFirst()){
            do{
                p.setID(cursor.getInt(cursor.getColumnIndex(Place.KEY_ID)));
                p.setName(cursor.getString(cursor.getColumnIndex(Place.KEY_name)));
                p.setDesc(cursor.getString(cursor.getColumnIndex(Place.KEY_desc)));
                p.setFacility(cursor.getString(cursor.getColumnIndex(Place.KEY_facility)));
                p.setLatitude(cursor.getDouble(cursor.getColumnIndex(Place.KEY_latitude)));
                p.setLongitude(cursor.getDouble(cursor.getColumnIndex(Place.KEY_longitude)));
                p.setRegion(cursor.getString(cursor.getColumnIndex(Place.KEY_region)));
                p.setSuburb(cursor.getString(cursor.getColumnIndex(Place.KEY_suburb)));
                p.setStreet(cursor.getString(cursor.getColumnIndex(Place.KEY_street)));
                p.setPostcode(cursor.getString(cursor.getColumnIndex(Place.KEY_postcode)));
                p.setOpen_time(cursor.getInt(cursor.getColumnIndex(Place.KEY_open_time)));
                p.setClose_time(cursor.getInt(cursor.getColumnIndex(Place.KEY_close_time)));
                p.setWeekday(p.int2bool(cursor.getInt(cursor.getColumnIndex(Place.KEY_weekday))));
                p.setWeekend(p.int2bool(cursor.getInt(cursor.getColumnIndex(Place.KEY_weekend))));
                p.setHoliday(p.int2bool(cursor.getInt(cursor.getColumnIndex(Place.KEY_holiday))));
                p.setWebsite(cursor.getString(cursor.getColumnIndex(Place.KEY_website)));
                p.setPix(Utils.Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(Place.KEY_pix))));

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return p;
    }

    //get places by activity id
    public ArrayList<Place> getPlaceListByActivity(int aId){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                Place.TABLE+"."+Place.KEY_ID+","+Place.TABLE+"."+Place.KEY_name+","+Place.TABLE+"."+Place.KEY_desc+","+
                Place.TABLE+"."+Place.KEY_pix+
                " FROM "+Place.TABLE+", "+ActivityPlace.TABLE+", "+Activity.TABLE+
                " WHERE "+ActivityPlace.TABLE+"."+ActivityPlace.KEY_activity_id+" = "+Activity.TABLE+"."+Activity.KEY_ID+
                " AND "+ActivityPlace.TABLE+"."+ActivityPlace.KEY_place_id+" = "+Place.TABLE+"."+Place.KEY_ID+
                " AND "+Activity.TABLE+"."+Activity.KEY_ID+" = "+aId;
        ArrayList<Place> placeList=new ArrayList<Place>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                Place p=new Place();
                p.setID(cursor.getInt(cursor.getColumnIndex(Place.KEY_ID)));
                p.setName(cursor.getString(cursor.getColumnIndex(Place.KEY_name)));
                p.setDesc(cursor.getString(cursor.getColumnIndex(Place.KEY_desc)));
                p.setPix(Utils.Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(Place.KEY_pix))));
                placeList.add(p);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return placeList;
    }

    //get places by activity id
    public ArrayList<String> getFacilityType(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT DISTINCT "+Place.KEY_facility+" FROM "+
                Place.TABLE;
        ArrayList<String> placeList=new ArrayList<String>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                placeList.add(cursor.getString(cursor.getColumnIndex(Place.KEY_facility)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return placeList;
    }

    //get all different regions for places
    public ArrayList<String> getPlaceListByActivity(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT DISTINCT "+Place.KEY_region+" FROM "+Place.TABLE;
        ArrayList<String> regionList=new ArrayList<String>();
        regionList.add("Region");
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                regionList.add(cursor.getString(cursor.getColumnIndex(Place.KEY_region)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return regionList;
    }

    //filter place
    public ArrayList<Place> searchPlace(int aID, Filter filter){
        String keyword = filter.getWord();
        String region = filter.getRegion();
        boolean air = filter.isHasAir();
        boolean land = filter.isHasLand();
        boolean sea = filter.isHasSea();
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT DISTINCT "+
                Place.TABLE+"."+Place.KEY_ID+","+Place.TABLE+"."+Place.KEY_name+","+Place.TABLE+"."+Place.KEY_desc+","+
                Place.TABLE+"."+Place.KEY_pix+
                " FROM "+ActivityPlace.TABLE+", "+ Place.TABLE+", "+Activity.TABLE+
                " WHERE "+ActivityPlace.TABLE+"."+ActivityPlace.KEY_activity_id+" = "+Activity.TABLE+"."+Activity.KEY_ID+
                " AND "+ActivityPlace.TABLE+"."+ActivityPlace.KEY_place_id+" = "+Place.TABLE+"."+Place.KEY_ID+
                " AND "+Activity.TABLE+"."+Activity.KEY_ID+" = "+aID+" ";
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

        ArrayList<Place> placeList=new ArrayList<Place>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                Place p=new Place();
                p.setID(cursor.getInt(cursor.getColumnIndex(Place.KEY_ID)));
                p.setName(cursor.getString(cursor.getColumnIndex(Place.KEY_name)));
                p.setDesc(cursor.getString(cursor.getColumnIndex(Place.KEY_desc)));
                p.setPix(Utils.Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(Place.KEY_pix))));
                placeList.add(p);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return placeList;
    }

}
