package jzha442.explorevic.model;

import android.graphics.Bitmap;

import jzha442.explorevic.utility.BitmapCache;
import jzha442.explorevic.utility.Utils;

/**
 * Created by Jiao on 14/04/2017.
 */

public class FavouriteItem {
    public static final int ITEM = 0;
    public static final int SECTION = 1;

    public final int type;
    public final String text;
    private int favouriteID = -1;

    public int sectionPosition;
    public int listPosition;

    public FavouriteItem(int type, String text) {
        this.type = type;
        this.text = text;
    }

    public FavouriteItem(int type, String text, int favouriteID) {
        this.type = type;
        this.text = text;
        this.favouriteID = favouriteID;
    }

    @Override public String toString() {
        return text;
    }

    public int getFavouriteID(){
        return favouriteID;
    }
}
