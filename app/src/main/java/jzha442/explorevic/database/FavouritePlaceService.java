package jzha442.explorevic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import jzha442.explorevic.model.Activity;
import jzha442.explorevic.model.ActivityPlace;
import jzha442.explorevic.model.FavouritePlace;
import jzha442.explorevic.model.Place;
import jzha442.explorevic.utility.Utils;

import static android.R.attr.id;
import static android.R.id.list;

/**
 * Created by Jiao on 11/04/2017.
 */

public class FavouritePlaceService {

    private DBHelper dbHelper;

    public FavouritePlaceService(Context context) {
        dbHelper = new DBHelper(context);
    }

    //Insert data
    public int insert(FavouritePlace a){
        //打开连接，写入数据
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        if(a.getAp_id()==-1){
            values.put(FavouritePlace.KEY_name,a.getName());
            values.put(FavouritePlace.KEY_desc,a.getDesc());
            values.put(FavouritePlace.KEY_region,a.getRegion());
            values.put(FavouritePlace.KEY_address,a.getAddress());
            values.put(FavouritePlace.KEY_latitude,a.getLatitude());
            values.put(FavouritePlace.KEY_longitude,a.getLongitude());
            values.put(FavouritePlace.KEY_activity,a.getActivity());
            values.put(FavouritePlace.KEY_facility,a.getFacility());
            values.put(FavouritePlace.KEY_pix, Utils.Bitmap2Bytes(a.getPix()));

        }else {
            values.put(FavouritePlace.KEY_name,a.getName());
            values.put(FavouritePlace.KEY_ap_id, a.getAp_id());
        }

        long a_Id=db.insert(FavouritePlace.TABLE,null,values);
        db.close();
        return (int)a_Id;
    }

    //Edit data
    public void updateFavourite(FavouritePlace fp) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.execSQL("UPDATE "+FavouritePlace.TABLE+" SET "
                        +FavouritePlace.KEY_name+"=?, "
                        +FavouritePlace.KEY_desc+"=?, "
                        +FavouritePlace.KEY_region+"=?, "
                        +FavouritePlace.KEY_address+"=?, "
                        +FavouritePlace.KEY_latitude+"=?, "
                        +FavouritePlace.KEY_longitude+"=?, "
                        +FavouritePlace.KEY_activity+"=?, "
                        +FavouritePlace.KEY_facility+"=?, "
                        +FavouritePlace.KEY_pix+"=? "
                        +" WHERE "+FavouritePlace.KEY_ID+"=?",
                new Object[]{fp.getName(), fp.getDesc(), fp.getRegion(),
                        fp.getAddress(), fp.getLatitude(), fp.getLongitude(),
                        fp.getActivity(), fp.getFacility(),
                        Utils.Bitmap2Bytes(fp.getPix()), fp.getID()});
        db.close();
    }

    //Delete data by place_id
    public void deletePlaceById(int apID) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM "+FavouritePlace.TABLE+" WHERE "+FavouritePlace.KEY_ap_id+"=?", new Object[]{apID});
        db.close();
    }

    //Delete data by favourite_place id
    public void deleteFavourtiePlaceById(int id) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM "+FavouritePlace.TABLE+" WHERE "+FavouritePlace.KEY_ID+"=?", new Object[]{id});
        db.close();
    }

    //get all favourite_places
    /*
    public ArrayList<FavouritePlace> getList(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT * FROM "+FavouritePlace.TABLE;
        ArrayList<FavouritePlace> list=new ArrayList<FavouritePlace>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                FavouritePlace p=new FavouritePlace();
                p.setID(cursor.getInt(cursor.getColumnIndex(FavouritePlace.KEY_ID)));
                p.setName(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_name)));
                p.setDesc(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_desc)));
                p.setAddress(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_address)));
                p.setLatitude(cursor.getDouble(cursor.getColumnIndex(FavouritePlace.KEY_latitude)));
                p.setLongitude(cursor.getDouble(cursor.getColumnIndex(FavouritePlace.KEY_longitude)));
                p.setActivity(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_activity)));
                p.setFacility(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_facility)));
                p.setPix(Utils.Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(Place.KEY_pix))));
                p.setAp_id(cursor.getInt(cursor.getColumnIndex(FavouritePlace.KEY_ap_id)));
                list.add(p);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    */

    public ArrayList<FavouritePlace> getLatLngList(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT * FROM "+FavouritePlace.TABLE+" WHERE "+FavouritePlace.KEY_latitude+"!=0 ";
        ArrayList<FavouritePlace> list=new ArrayList<FavouritePlace>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                FavouritePlace p=new FavouritePlace();
                p.setID(cursor.getInt(cursor.getColumnIndex(FavouritePlace.KEY_ID)));
                //p.setName(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_name)));
                //p.setDesc(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_desc)));
                //p.setAddress(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_address)));
                p.setLatitude(cursor.getDouble(cursor.getColumnIndex(FavouritePlace.KEY_latitude)));
                p.setLongitude(cursor.getDouble(cursor.getColumnIndex(FavouritePlace.KEY_longitude)));
                //p.setActivity(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_activity)));
                //p.setFacility(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_facility)));
                //p.setPix(Utils.Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(Place.KEY_pix))));
                //p.setAp_id(cursor.getInt(cursor.getColumnIndex(FavouritePlace.KEY_ap_id)));
                list.add(p);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    //get custom favourite places by region
    public ArrayList<FavouritePlace> getCustomFavouriteByRegion(String region){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT * FROM " + FavouritePlace.TABLE +
                " WHERE "+ FavouritePlace.KEY_region+" = '"+region+"'";
        ArrayList<FavouritePlace> list=new ArrayList<FavouritePlace>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                FavouritePlace p=new FavouritePlace();
                p.setID(cursor.getInt(cursor.getColumnIndex(FavouritePlace.KEY_ID)));
                p.setName(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_name)));
                p.setDesc(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_desc)));
                p.setRegion(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_region)));
                p.setAddress(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_address)));
                p.setLatitude(cursor.getDouble(cursor.getColumnIndex(FavouritePlace.KEY_latitude)));
                p.setLongitude(cursor.getDouble(cursor.getColumnIndex(FavouritePlace.KEY_longitude)));
                p.setActivity(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_activity)));
                p.setFacility(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_facility)));
                p.setPix(Utils.Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(Place.KEY_pix))));
                p.setAp_id(cursor.getInt(cursor.getColumnIndex(FavouritePlace.KEY_ap_id)));
                list.add(p);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    //get favourite places by region with activity, place, activity_places table
    public ArrayList<FavouritePlace> getFavouritePlacesByRegion(String region){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                FavouritePlace.TABLE+"."+FavouritePlace.KEY_ID+", "+
                FavouritePlace.TABLE+"."+FavouritePlace.KEY_name+", "+
                FavouritePlace.TABLE+"."+FavouritePlace.KEY_ap_id+
                " FROM "+Place.TABLE+", "+FavouritePlace.TABLE+", "+ ActivityPlace.TABLE+
                " WHERE "+ActivityPlace.TABLE+"."+ActivityPlace.KEY_ID+" = "+FavouritePlace.TABLE+"."+FavouritePlace.KEY_ap_id+
                " AND "+Place.TABLE+"."+Place.KEY_ID+" = "+ActivityPlace.TABLE+"."+ActivityPlace.KEY_place_id+
                " AND "+Place.TABLE+"."+Place.KEY_region+" = '"+region+"'";
        ArrayList<FavouritePlace> list=new ArrayList<FavouritePlace>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                FavouritePlace p=new FavouritePlace();
                p.setID(cursor.getInt(cursor.getColumnIndex(FavouritePlace.KEY_ID)));
                p.setName(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_name)));
                //p.setDesc(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_desc)));
                //p.setAddress(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_address)));
                //p.setLatitude(cursor.getDouble(cursor.getColumnIndex(FavouritePlace.KEY_latitude)));
                //p.setLongitude(cursor.getDouble(cursor.getColumnIndex(FavouritePlace.KEY_longitude)));
                //p.setPix(Utils.Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(Place.KEY_pix))));
                p.setAp_id(cursor.getInt(cursor.getColumnIndex(FavouritePlace.KEY_ap_id)));
                list.add(p);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    //get all different regions for places
    public ArrayList<String> getRegionList(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT DISTINCT "+Place.TABLE+"."+Place.KEY_region+
                " FROM "+Place.TABLE+", "+FavouritePlace.TABLE+", "+ ActivityPlace.TABLE+
                " WHERE "+ActivityPlace.TABLE+"."+ActivityPlace.KEY_ID+" = "+FavouritePlace.TABLE+"."+FavouritePlace.KEY_ap_id+
                " AND "+Place.TABLE+"."+Place.KEY_ID+" = "+ActivityPlace.TABLE+"."+ActivityPlace.KEY_place_id+
                " ORDER BY "+Place.TABLE+"."+Place.KEY_region;
        ArrayList<String> regionList=new ArrayList<String>();
        //regionList.add("Region");
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                regionList.add(cursor.getString(cursor.getColumnIndex(Place.KEY_region)));
            }while(cursor.moveToNext());
        }
        cursor.close();

        String selectQuery2="SELECT DISTINCT "+FavouritePlace.KEY_region+
                " FROM "+FavouritePlace.TABLE+
                " ORDER BY "+FavouritePlace.KEY_region;
        Cursor cursor2=db.rawQuery(selectQuery2,null);

        if(cursor2.moveToFirst()){
            do{
                String region = cursor2.getString(cursor2.getColumnIndex(FavouritePlace.KEY_region));
                if(region != null && !region.isEmpty() && !regionList.contains(region)) {
                    regionList.add(region);
                }
            }while(cursor2.moveToNext());
        }
        cursor2.close();
        db.close();
        return regionList;
    }

    //get one favourite place by id
    public FavouritePlace getPlaceById(int Id){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT * FROM " + FavouritePlace.TABLE
                + " WHERE " +
                FavouritePlace.KEY_ID + "=?";
        FavouritePlace p=new FavouritePlace();
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(Id)});
        if(cursor.moveToFirst()){
            do{
                p.setID(cursor.getInt(cursor.getColumnIndex(FavouritePlace.KEY_ID)));
                p.setName(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_name)));
                int apID = cursor.getInt(cursor.getColumnIndex(FavouritePlace.KEY_ap_id));
                p.setAp_id(apID);
                if(apID<=0){
                    p.setDesc(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_desc)));
                    p.setRegion(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_region)));
                    p.setAddress(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_address)));
                    p.setLatitude(cursor.getDouble(cursor.getColumnIndex(FavouritePlace.KEY_latitude)));
                    p.setLongitude(cursor.getDouble(cursor.getColumnIndex(FavouritePlace.KEY_longitude)));
                    p.setActivity(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_activity)));
                    p.setFacility(cursor.getString(cursor.getColumnIndex(FavouritePlace.KEY_facility)));
                    p.setPix(Utils.Bytes2Bimap(cursor.getBlob(cursor.getColumnIndex(FavouritePlace.KEY_pix))));
                }

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return p;
    }

    //get one favourite place by id
    public boolean checkPlaceById(int apID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + FavouritePlace.TABLE
                + " WHERE " +
                FavouritePlace.KEY_ap_id + "=?";
        FavouritePlace p = new FavouritePlace();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(apID)});
        if(cursor.getCount()>0){
            return true;
        }else{
            return false;
        }
    }
}
