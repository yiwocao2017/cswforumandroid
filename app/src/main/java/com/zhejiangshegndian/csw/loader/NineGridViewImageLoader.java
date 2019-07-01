package com.zhejiangshegndian.csw.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzy.ninegrid.NineGridView;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.tool.Constants;

/**
 * Created by LeiQ on 2017/4/17.
 */

public class NineGridViewImageLoader implements NineGridView.ImageLoader {


    @Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {
        if (url.indexOf("http") != -1) {
            Glide.with(context)
                    .load(url)
                    .error(R.mipmap.default_pic)
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(Constants.IMAGE + url )
                    .error(R.mipmap.default_pic)
                    .into(imageView);
        }
    }

    @Override
    public Bitmap getCacheImage(String url) {
        return null;
    }
}
