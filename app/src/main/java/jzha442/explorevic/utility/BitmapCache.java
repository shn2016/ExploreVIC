package jzha442.explorevic.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Hashtable;

import jzha442.explorevic.R;

/**
 * Created by Jiao on 04/04/2017.
 */

public class BitmapCache {
    public static BitmapCache instance;

    public static BitmapCache getInstance() {
        if (instance == null) {
            instance = new BitmapCache();
        }
        return instance;
    }

    private LruCache<String, Bitmap> mImageCache;

    private BitmapCache() {
        // 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。
        // LruCache通过构造函数传入缓存值，以KB为单位。
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 使用最大可用内存值的1/8作为缓存的大小。
        int cacheSize = maxMemory / 8;
        mImageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 重写此方法来衡量每张图片的大小，默认返回图片数量。
                return   bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToCache(String path, Bitmap bitmap) {

        if(getBitmapByPath(path)!=null)
        {
            mImageCache.remove(path);
        }
        mImageCache.put(path, bitmap);
    }

    public Bitmap getBitmapByPath(String path) {

        return mImageCache.get(path);
    }


    public void removeBitmap(String path)
    {
        Bitmap bitmap=getBitmapByPath(path);
        if(bitmap!=null&&!bitmap.isRecycled())
        {
            bitmap.recycle();
        }
        mImageCache.remove(path);
    }

    public void clearCache() {
        if (mImageCache != null) {
            if (mImageCache.size() > 0) {
                Log.d("CacheUtils",
                        "mMemoryCache.size() " + mImageCache.size());
                mImageCache.evictAll();
                Log.d("CacheUtils", "mMemoryCache.size()" + mImageCache.size());
            }
            mImageCache = null;
        }
    }

    public void saveDrawableImageToCahche(String key, ImageView imageview){
        //To get the bitmap from the imageView
        Bitmap bitmap = ((BitmapDrawable)imageview.getDrawable()).getBitmap();

        //Saving bitmap to cache. it will later be retrieved using the bitmap_image key
        addBitmapToCache(key, bitmap);
    }
}
