package com.zhejiangshegndian.csw.wxapi;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.tool.Constants;


/**
 * 微信支付结果界面
 * @author hfk
 * create at 2016/5/3 10:42
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    private static WXCallBack wxCallBack;

    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wx_entry);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID_WX);
        api.handleIntent(getIntent(), this);

        preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    /**
     * 结果回调方法
     * @param resp
     */
    @Override
    public void onResp(BaseResp resp) {
        System.out.println("resp.errCode"+resp.errCode);
        System.out.println("resp.errStr"+resp.errStr);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if(resp.errCode == 0){
                Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            } else if(resp.errCode == -2) {
                Toast.makeText(WXPayEntryActivity.this, "支付取消", Toast.LENGTH_SHORT).show();
            } else {
//                checkPayResult(resp.errCode);
            }
            finish();
        }
    }


    public static void setWXCallBack(WXCallBack callBack){
        wxCallBack=callBack;
    }

    public interface WXCallBack {
        void msgCallBask(int msg);
    }
}