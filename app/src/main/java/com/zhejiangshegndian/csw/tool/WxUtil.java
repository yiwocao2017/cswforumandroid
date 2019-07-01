package com.zhejiangshegndian.csw.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhejiangshegndian.csw.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * Created by LeiQ on 2017/1/10.
 */

public class WxUtil {

    private static IWXAPI api;

    public static IWXAPI registToWx(Context context){
        api = WXAPIFactory.createWXAPI(context, Constants.APP_ID_WX);
        api.registerApp(Constants.APP_ID_WX);

        return api;
    }

    /**
     *  检测是否有微信与是否支持微信支付
     * @return
     */
    public static boolean check(Context context) {

        api = registToWx(context);

        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
//        boolean isPaySupported = api.isWXAppInstalled() && api.isWXAppSupportAPI();
        if(!api.isWXAppInstalled())
        {
            Toast.makeText(context,"没有安装微信，不能分享到朋友圈",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!api.isWXAppSupportAPI())
        {
            Toast.makeText(context,"你使用的微信版本不支持微信支付！",Toast.LENGTH_SHORT).show();
            return false;
        }
        return isPaySupported;
    }

    public static void signinToWx(){
        {
            // send oauth request
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test";
            api.sendReq(req);
        }
    }


    public static void share(final Context context, View view, final String url, final String title, final String description, final String imgUrl) {

        // 一个自定义的布局，作为显示的内容
        View mview = LayoutInflater.from(context).inflate(R.layout.popup_share, null);

        ImageView wx = (ImageView) mview.findViewById(R.id.img_wx);
        ImageView pyq = (ImageView) mview.findViewById(R.id.img_pyq);
        TextView qx = (TextView) mview.findViewById(R.id.txt_cancel);

        final PopupWindow popupWindow = new PopupWindow(mview,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        wx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                WxUtil.shareToWX(context, url, title, description, imgUrl);
                popupWindow.dismiss();
            }
        });

        pyq.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                WxUtil.shareToPYQ(context, url, title, description, imgUrl);
                popupWindow.dismiss();

            }
        });
        qx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.corners_share));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    /**
     *  分享到朋友圈
     * @param
     */
    public static void shareToPYQ(Context context, String url, String title, String description, String imgUrl) {

        api = registToWx(context);

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;

        try {
            Bitmap thumbBmp;
            if(imgUrl.equals("")){
                Bitmap bmp1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);
                thumbBmp = Bitmap.createScaledBitmap(bmp1, 100, 100, true);
            }else {
                //压缩Bitmap
                thumbBmp =Bitmap.createScaledBitmap(GetLocalOrNetBitmap(imgUrl), 100, 100, true);
            }
            msg.thumbData = Bitmap2Bytes(thumbBmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("图文链接");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    /**
     *  分享微信聊天界面
     * @param
     */
    public static void shareToWX(Context context, String url, String title, String description, String imgUrl) {

        api = registToWx(context);

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;



        try {
            Bitmap thumbBmp;
            if(imgUrl.equals("")){
                Bitmap bmp1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);
                thumbBmp = Bitmap.createScaledBitmap(bmp1, 100, 100, true);
            }else {
                //压缩Bitmap
                thumbBmp =Bitmap.createScaledBitmap(getBitmap(imgUrl), 100, 100, true);
            }

            msg.thumbData = Bitmap2Bytes(thumbBmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("图文链接");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    /**
     * 构造一个用于请求的唯一标识
     *
     * @param type 分享的内容类型
     * @return
     */
    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 微信 支付
     * @param object
     * @return
     */
    public static void pay(Context context,JSONObject object){

        api = registToWx(context);

        String sign = "";
        String appid = "";
        String noncestr = "";
        String timestamp = "";
        String partnerid = "";
        String prepayid = "";
        String wechatPackage ="";

        try {
            sign = object.getString("sign");
            appid = object.getString("appId");
            prepayid = object.getString("prepayId");
            noncestr = object.getString("nonceStr");
            timestamp = object.getString("timeStamp");
            partnerid = object.getString("partnerid");
            wechatPackage = object.getString("wechatPackage");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("sign="+sign);
        System.out.println("appId="+appid);
        System.out.println("prepayid="+prepayid);
        System.out.println("noncestr="+noncestr);
        System.out.println("timestamp="+timestamp);
        System.out.println("partnerid="+partnerid);
        System.out.println("wechatPackage="+wechatPackage);

        PayReq req = new PayReq();
        req.sign = sign;
        req.appId = appid;
        req.nonceStr = noncestr;
        req.prepayId = prepayid;
        req.partnerId = partnerid;
        req.timeStamp = timestamp;
        req.packageValue = wechatPackage;

        api.sendReq(req);

    }

    /**
     * 微信 支付签名
     * @param params
     * @return
     */
    public static String createSign(Map<String,String> params){
        Set<String> keysSet = params.keySet();
        //对参数进行排序
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        //签名参数字符串
        String signStr = "";
        int index = 0;
        for (Object key : keys){
            if(key.equals("sign"))
                continue;
            String value = params.get(key);
            signStr += key + "=" + value;
            index ++ ;
            if(keys.length > index)
                signStr += "&";
        }
        String sign = signStr + "&key=7fx5r0n7j8k1tvnzpn55c3ef0zo9a7be";
        return MD5Util.MD5Encode(sign, "utf-8").toUpperCase();
    }

    /**
     * 把网络资源图片转化成bitmap
     * @param url  网络资源图片
     * @return  Bitmap
     */
    public static Bitmap GetLocalOrNetBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new URL(url).openStream(), 1024);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, 1024);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            data = null;
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }


    public static Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            int length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

}
