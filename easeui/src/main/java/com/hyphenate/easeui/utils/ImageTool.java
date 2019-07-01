package com.hyphenate.easeui.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hyphenate.easeui.R;

public class ImageTool {

    /** 七牛路径 */
    public static String IMAGE = "http://oigx51fc5.bkt.clouddn.com/";
    // 无损压缩
    public static String COMPRESS = "?imageslim";

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
                    .load(R.drawable.ease_default_avatar)
                    .transform(new GlideCircleTransform(context))
                    .into(sellersmallimg);

        } else if (url.indexOf("http") != -1) {
            Glide.with(context)
                    .load(url)
                    .error(R.drawable.ease_default_avatar)
                    .transform(new GlideCircleTransform(context))
                    .into(sellersmallimg);
            System.out.println("glide + uri ="+ url);
        } else {
            Glide.with(context)
                    .load(IMAGE + url + COMPRESS)
                    .error(R.drawable.ease_default_avatar)
                    .transform(new GlideCircleTransform(context))
                    .into(sellersmallimg);
            System.out.println("glide + uri ="+IMAGE + url + COMPRESS );
        }


    }
}
