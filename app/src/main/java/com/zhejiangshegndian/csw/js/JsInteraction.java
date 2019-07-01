package com.zhejiangshegndian.csw.js;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.zhejiangshegndian.csw.activity.WebActivity;
import com.zhejiangshegndian.csw.tool.WxUtil;
import com.zhejiangshegndian.csw.tool.Xutil;
import com.zhejiangshegndian.csw.tool.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LeiQ on 2017/5/17.
 */

public class JsInteraction {

    View view;
    Context context;
    SharedPreferences userInfoSp;

    public JsInteraction(Context context, View view){
        this.view = view;
        this.context = context;

        userInfoSp = context.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
    }

    @JavascriptInterface
    public void pay(String code) {
        JSONObject object = new JSONObject();
        try {
            object.put("orderCode", code);
            object.put("payType", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_660021, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    System.out.println(WxUtil.check(context));

                    if (WxUtil.check(context)) {
                        WxUtil.pay(context, jsonObject);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(context, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *
     * @param url 活动连接
     * @param title 活动标题
     * @param description 活动描述
     * @param imgUrl 活动图片连接
     */
    @JavascriptInterface
    public void share(String url, String title, String description, String imgUrl) {

        WxUtil.share(context, view, url, title, description, imgUrl);
    }

    @JavascriptInterface
    public void back() {
        WebActivity activity = (WebActivity) context;
        activity.finish();
    }


}
