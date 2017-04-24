package jzha442.explorevic.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

/**
 * Created by Jiao on 24/03/2017.
 */

public class Activity {
    //table
    public static final String TABLE="Activity";
    public static String[] activityType = {"Sky", "Mountain", "Sea"};

    //attributes' name in database
    public static final String KEY_ID="id";
    public static final String KEY_name="name";
    public static final String KEY_desc="desc";//facility needed
    public static final String KEY_warn="warn";
    public static final String KEY_type="type";
    public static final String KEY_pix="pix";

    //attributes
    private int ID;
    private String name;
    private String desc;
    private String warn;
    private String type;
    private Bitmap pix;
    //byte[] blob=c.getBlob("xxx");
    //Bitmap bmp= BitmapFactory.decodeByteArray(blob,0,blob.length);
    //ImageView.setImageBitmap(bmp);

    //constructor

    public Activity() {
    }

    public Activity(int ID, String name, String desc, String warn, String type, Bitmap pix) {
        this.ID = ID;
        this.name = name;
        this.desc = desc;
        this.warn = warn;
        this.type = type;
        this.pix = pix;
    }

    //setter and getter

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

    public String getWarn() {
        return warn;
    }

    public void setWarn(String warn) {
        this.warn = warn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Bitmap getPix() {
        return pix;
    }

    public void setPix(Bitmap pix) {
        this.pix = pix;
    }

    //print activity to string
    @Override
    public String toString() {
        return "Activity{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", warn='" + warn + '\'' +
                ", type='" + type + '\'' +
                ", pix=" + pix +
                '}';
    }
}
