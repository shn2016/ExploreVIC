package jzha442.explorevic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import jzha442.explorevic.model.Activity;
import jzha442.explorevic.model.SearchRecord;

/**
 * Created by Jiao on 26/03/2017.
 */

public class SearchRecordService {
    private DBHelper dbHelper;

    public SearchRecordService(Context context) {
        dbHelper = new DBHelper(context);
    }

    //Insert data
    public int insert(String sr){

        //check if the word (sr) exists
        SearchRecord record = getSearchRecordByWord(sr);
        if(record.getID()>0){
            return record.getID();
        }
        //打开连接，写入数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SearchRecord.KEY_word, sr);

        long a_Id = db.insert(SearchRecord.TABLE, null, values);
        db.close();
        return (int) a_Id;
    }

    //update data
    /*
    public void update(SearchRecord sr) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SearchRecord.KEY_word, sr.getWord());

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(SearchRecord.TABLE, values, SearchRecord.KEY_ID + "= ?", new String[] { String.valueOf(sr.getID()) });
        db.close(); // Closing database connection
    }
    */

    //get all records
    public ArrayList<String> getSearchList(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT * FROM "+SearchRecord.TABLE;
        ArrayList<String> searchList=new ArrayList<String>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                searchList.add(cursor.getString(cursor.getColumnIndex(SearchRecord.KEY_word)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return searchList;
    }

    //get latest 5 records
    public ArrayList<String> getLatestSearchList(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT * FROM "+SearchRecord.TABLE+" ORDER BY "+SearchRecord.KEY_ID+" DESC LIMIT 3";
        ArrayList<String> searchList=new ArrayList<String>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                searchList.add(cursor.getString(cursor.getColumnIndex(SearchRecord.KEY_word)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return searchList;
    }

    //get similar records
    public ArrayList<SearchRecord> getSimilarSearchList(String word){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT * FROM "+SearchRecord.TABLE+" WHERE "+SearchRecord.KEY_word+" LIKE '%"+word+"%' ORDER BY "+SearchRecord.KEY_word;
        ArrayList<SearchRecord> searchList=new ArrayList<SearchRecord>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                SearchRecord sr=new SearchRecord();
                sr.setID(cursor.getInt(cursor.getColumnIndex(SearchRecord.KEY_ID)));
                sr.setWord(cursor.getString(cursor.getColumnIndex(SearchRecord.KEY_word)));
                searchList.add(sr);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return searchList;
    }

    //find by word
    private SearchRecord getSearchRecordByWord(String word){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT * FROM "+SearchRecord.TABLE
                + " WHERE " +
                SearchRecord.KEY_word + "=?";
        SearchRecord sr=new SearchRecord();
        Cursor cursor=db.rawQuery(selectQuery,new String[]{word});

        if(cursor.moveToFirst()){
            do{
                sr.setID(cursor.getInt(cursor.getColumnIndex(SearchRecord.KEY_ID)));
                sr.setWord(cursor.getString(cursor.getColumnIndex(SearchRecord.KEY_word)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return sr;

    }

    //clear data
    public void deleteData() {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.execSQL("delete from "+SearchRecord.TABLE);
        db.close();
    }
}
