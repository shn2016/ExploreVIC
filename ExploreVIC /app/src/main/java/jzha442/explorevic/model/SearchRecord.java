package jzha442.explorevic.model;

import android.graphics.Bitmap;

/**
 * Created by Jiao on 26/03/2017.
 */

public class SearchRecord {
    //table
    public static final String TABLE="Search_Records";

    //attributes' name in database
    public static final String KEY_ID="id";
    public static final String KEY_word="word";

    //attributes
    private int ID;
    private String word;

    //constructor

    public SearchRecord() {
    }

    public SearchRecord(String word) {
        this.word = word;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    @Override
    public String toString() {
        return "SearchRecord{" +
                "ID=" + ID +
                ", word='" + word + '\'' +
                '}';
    }
}
