package jzha442.explorevic.utility;

import android.net.Uri;
import android.os.Environment;
import android.view.View;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TakePhotoOptions;

import java.io.File;

import jzha442.explorevic.R;


/**
 * - 支持通过相机拍照获取图片
 * - 支持从相册选择图片
 * - 支持从文件选择图片
 * - 支持多图选择
 * - 支持批量图片裁切
 * - 支持批量图片压缩
 * - 支持对图片进行压缩
 * - 支持对图片进行裁剪
 * - 支持对裁剪及压缩参数自定义
 * - 提供自带裁剪工具(可选)
 * - 支持智能选取及裁剪异常处理
 * - 支持因拍照Activity被回收后的自动恢复
 * Author: crazycodeboy
 * Date: 2016/9/21 0007 20:10
 * Version:4.0.0
 * 技术博文：http://www.cboy.me
 * GitHub:https://github.com/crazycodeboy
 * Eamil:crazycodeboy@gmail.com
 */
public class PhotoHelper {
    private View rootView;
    private int cropHeight;
    private int cropWidth;
    public static PhotoHelper of(View rootView){
        return new PhotoHelper(rootView);
    }
    private PhotoHelper(View rootView) {
        this.rootView = rootView;
    }

    public void onClick(View view, TakePhoto takePhoto) {
        File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+ System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);

        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);
        //button fucntion - take or pick photo
        switch (view.getId()){
            case R.id.btnPickBySelect:
                int limit= 1;

                //takePhoto.onPickFromGallery();
                takePhoto.onPickFromGalleryWithCrop(imageUri,getCropOptions());
                break;
            case R.id.btnPickByTake:
                takePhoto.onPickFromCaptureWithCrop(imageUri,getCropOptions());
                //takePhoto.onPickFromCapture(imageUri);
                break;
            default:
                break;
        }
    }

    private void configTakePhotoOption(TakePhoto takePhoto){
        TakePhotoOptions.Builder builder=new TakePhotoOptions.Builder();
        //builder.setWithOwnGallery(true);
        takePhoto.setTakePhotoOptions(builder.create());
    }

    private void configCompress(TakePhoto takePhoto){
        /*
        if(rgCompress.getCheckedRadioButtonId()!=R.id.rbCompressYes){
            takePhoto.onEnableCompress(null,false);
            return ;
        }
        */
        int maxSize= 102400;
        int width= 400;
        int height= 400;
        boolean showProgressBar = true;
        boolean enableRawFile = false;
        CompressConfig config;
        /*
        if(rgCompressTool.getCheckedRadioButtonId()!=R.id.rbCompressWithOwn){
            LubanOptions option=new LubanOptions.Builder()
                    .setMaxHeight(height)
                    .setMaxWidth(width)
                    .setMaxSize(maxSize)
                    .create();
            config= CompressConfig.ofLuban(option);
            config.enableReserveRaw(enableRawFile);
        }*/
        config=new CompressConfig.Builder()
                .setMaxSize(maxSize)
                .setMaxPixel(width>=height? width:height)
                .enableReserveRaw(enableRawFile)
                .create();
        takePhoto.onEnableCompress(config,showProgressBar);

    }

    private CropOptions getCropOptions(){
        int height= 300;
        int width= 400;
        boolean withWonCrop=false;

        CropOptions.Builder builder=new CropOptions.Builder();

        //builder.setAspectX(width).setAspectY(height);//fix ratio of height and width
        builder.setOutputX(width).setOutputY(height);

        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }

}
