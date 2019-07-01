package com.zhejiangshegndian.csw.loader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by dell1 on 2016/12/13.
 */

public class BannerImageLoader extends ImageLoader {

    public static String IMAGE = "http://oigx51fc5.bkt.clouddn.com/";

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        if (path.toString().indexOf("http") != -1) {
            Glide.with(context)
                    .load(path.toString())
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(IMAGE + path.toString() )
                    .into(imageView);
        }

    }
}
