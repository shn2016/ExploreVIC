package jzha442.explorevic.model;

import android.graphics.Bitmap;

/**
 * Created by Jiao on 24/03/2017.
 */

public class ActivityPlace {
    //table
    public static final String TABLE="Activity_Places";

    //attributes' name in database
    public static final String KEY_ID="id";
    public static final String KEY_name="name";
    public static final String KEY_desc="desc";
    public static final String KEY_min_cost="min_cost";
    public static final String KEY_max_cost="max_cost";
    public static final String KEY_pix="pix";
    public static final String KEY_activity_id="activity_id";
    public static final String KEY_place_id="place_id";

    //attributes
    private int ID;
    private String name;
    private String desc;
    private int min_cost;
    private int max_cost;
    private Bitmap pix;
    private int activity_id;
    private int place_id;

    //constructor

    public ActivityPlace() {
    }

    public ActivityPlace(int ID, String name, String desc, Bitmap pix, int activity_id, int place_id) {
        this.ID = ID;
        this.name = name;
        this.desc = desc;
        this.pix = pix;
        this.activity_id = activity_id;
        this.place_id = place_id;
    }

    //getter and setter

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getMin_cost() {
        return min_cost;
    }

    public void setMin_cost(int min_cost) {
        this.min_cost = min_cost;
    }

    public int getMax_cost() {
        return max_cost;
    }

    public void setMax_cost(int max_cost) {
        this.max_cost = max_cost;
    }

    public Bitmap getPix() {
        return pix;
    }

    public void setPix(Bitmap pix) {
        this.pix = pix;
    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    //print ActivityPlace to string
    @Override
    public String toString() {
        return "ActivityPlace{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", pix=" + pix +
                ", activity_id='" + activity_id + '\'' +
                ", place_id='" + place_id + '\'' +
                '}';
    }
}
