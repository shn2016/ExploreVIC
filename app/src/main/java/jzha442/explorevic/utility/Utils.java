package jzha442.explorevic.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;

import jzha442.explorevic.R;
import jzha442.explorevic.model.PlaceWeather;

import static android.R.attr.path;
import static android.R.id.list;

public class Utils {
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    public static boolean isConnectedNetwork (Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo () != null && cm.getActiveNetworkInfo ().isConnectedOrConnecting ();
    }

    public static void showMessage(Context context, String msg){
        Toast toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setText(msg);
        toast.show();
    }

    public  static String cleanString(String s){
        s = s.replaceAll(".*([';]+|(--)+).*", " ");
        s = s.trim();
        while (s.startsWith("　")) {//这里判断是不是全角空格
            s = s.substring(1, s.length()).trim();
        }
        while (s.endsWith("　")) {
            s = s.substring(0, s.length() - 1).trim();
        }
        return s;
    }

    /**

     * 从指定的url中获取字节数组

     *

     * @param urlPath

     * @return 字节数组

     * @throws Exception

     */

    public static byte[] readParse(String urlPath) throws Exception {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte[] data = new byte[1024];

        int len = 0;

        URL url = new URL(urlPath);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        InputStream inStream = conn.getInputStream();

        while ((len = inStream.read(data)) != -1) {

            outStream.write(data, 0, len);

        }

        inStream.close();

        return outStream.toByteArray();

    }

    //convert picture between Bitmap and byte[]

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    //Bitmap缩放
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    public static String getFaciIconName(String f){
        String faci = f.toLowerCase();
        faci = faci.replace(' ', '_');
        //faci += ".png";

        return faci;
    }

    public static int getImageResourceId(Context context, String name) {
        int id = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        return id;
    }

    public static void storeWeatherToFile(Context context, ArrayList<PlaceWeather> pwList, String prefix){
        String jsonData = convertPWListToJson(pwList);
        File file = getWeatherFile(context, prefix);

        if (!file.exists()) {
            try {
                //在指定的文件夹中创建文件
                file.createNewFile();
            } catch (Exception e) {
            }
        }else{
            return;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(jsonData.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void CreateDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                Log.e("Utils", "error on creat dirs:" + e.getStackTrace());
            }
        }
    }

    public static File getWeatherFile(Context context, String prefix){
        String path = context.getCacheDir()+"/weather";
        CreateDir(path);
        String filename = prefix+Timeconversion.getCurrentDate()+".json";
        File file = new File(path+"/"+filename);
        return file;
    }

    private static String convertPWListToJson(ArrayList<PlaceWeather> pwList){
        Gson gson = new Gson();
        String jsonInString = gson.toJson(pwList);
        return jsonInString;
    }

    //file path is absolutely path
    public static ArrayList<PlaceWeather> getWeatherFromFile(Context context, String prefix) throws FileNotFoundException {
        String filepath = getWeatherFile(context, prefix).getAbsolutePath();
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<PlaceWeather>>(){}.getType();
        ArrayList<PlaceWeather> list = gson.fromJson(new FileReader(filepath), type);
        return list;
    }

    //check if there is weather cache file up to date
    public static boolean hasWeatherCache(Context context, String prefix){
        File file = getWeatherFile(context, prefix);

        if (file.exists()) {
            return true;
        }else {

            String path = context.getCacheDir() + "/weather";
            File weatherFolder = new File(path);

            if (weatherFolder.exists()) {
                weatherFolder.delete();
            }


            return false;
        }
    }

    public static ArrayList<String> getSingleElement(String text){
        String[] array = text.split(",");
        ArrayList<String> newArray = new ArrayList<String>();
        for(String f:array){
            if(f != null && !f.isEmpty() && !f.trim().isEmpty()) {
                newArray.add(f.trim());
            }
        }
        return newArray;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static String Array2String(ArrayList<String> array){
        String s = "";
        for(String a: array){
            a = a.trim();
            if(!a.isEmpty()) {
                s = s + a + ",";
            }
        }
        if (s.length() > 0 && s.charAt(s.length()-1)==',') {
            s = s.substring(0, s.length()-1);
        }
        return s;
    }

}
