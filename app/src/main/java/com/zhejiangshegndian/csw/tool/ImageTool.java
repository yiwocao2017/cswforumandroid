package com.zhejiangshegndian.csw.tool;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhejiangshegndian.csw.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import static com.zhejiangshegndian.csw.tool.Constants.COMPRESS;
import static com.zhejiangshegndian.csw.tool.Constants.IMAGE;

/**
 * Created by LeiQ on 2017/4/14.
 */

public class ImageTool {

    // 图片选择flag
    public static final int RESULT_LOAD_IMAGE = 8888;
    public static final int RESULT_CAMARA_IMAGE = 9999;

    /**
     * @param context
     * @param url
     * @param sellersmallimg
     * glide图片加载
     */
    public static void glide(Context context, String url, ImageView sellersmallimg){
        if (url == null || url.indexOf("www")!=-1) {
            return;
        }
        if (url.indexOf("storage") != -1 || url.indexOf("system") != -1 || url.indexOf("sdcard") != -1) {
            Glide.with(context).load(url)
                    .into(sellersmallimg);
        }else if (url.indexOf("http") != -1) {
            Glide.with(context).load(url)

                    .into(sellersmallimg);
        } else {
            Glide.with(context)
                    .load(IMAGE + url + COMPRESS)
                    .into(sellersmallimg);

            System.out.println("glide + uri ="+IMAGE + url + COMPRESS );

        }
    }

    /**
     * @param context
     * @param url
     * @param sellersmallimg
     * glide图片加载
     */
    public static void photo(Context context, String url, ImageView sellersmallimg){
        if (url == null || url.indexOf("www")!=-1) {
            return;
        }
        if (url.equals("")){
            Glide.with(context)
                    .load(R.mipmap.photo_default)
                    .into(sellersmallimg);

        } else if (url.indexOf("http") != -1) {
            Glide.with(context)
                    .load(url)
                    .error(R.mipmap.photo_default)
                    .into(sellersmallimg);
        } else {
            Glide.with(context)
                    .load(IMAGE + url + COMPRESS)
                    .error(R.mipmap.photo_default)
                    .into(sellersmallimg);

            System.out.println("glide + uri ="+IMAGE + url + COMPRESS );

        }
    }

    /**
     * 调用系统相册的操作,在onActivityResult中调用
     *
     * @param data onActivityResult中的Intent
     */
    public static String album(Context context,Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        Log.d("picturePath", picturePath);
        BitmapFactory.decodeFile(picturePath);
        return picturePath;
    }

    /**
     * 调用系统相机,在onActivityResult中调用，拍照后保存到sdcard卡中
     *
     * @param data onActivityResult中的Intent
     * @return
     */
    public static String camara(Context context,Intent data) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.i("TestFile", "SD card is not avaiable/writeable right now.");
            Toast.makeText(context,
                    "SD card is not avaiable/writeable right now.",
                    Toast.LENGTH_LONG).show();
            return null;
        }
        String name = new DateFormat().format("yyyyMMdd_hhmmss",
                Calendar.getInstance(Locale.CHINA))
                + ".jpg";
        Bundle bundle = data.getExtras();
        String fileName = "";
        if (bundle != null) {
            Bitmap bitmap = (Bitmap) bundle.get("data");
            FileOutputStream b = null;
            File file = new File("sdcard/DCIM/Camera/");
            file.mkdirs();// 创建文件夹
            fileName = "sdcard/DCIM/Camera/" + name;
            try {
                b = new FileOutputStream(fileName);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return fileName;
    }

}
