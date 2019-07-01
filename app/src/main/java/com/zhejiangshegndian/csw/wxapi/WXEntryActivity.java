package com.zhejiangshegndian.csw.wxapi;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.activity.BindPhoneActivity;
import com.zhejiangshegndian.csw.activity.MainActivity;
import com.zhejiangshegndian.csw.model.CityModel;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.MapTool;
import com.zhejiangshegndian.csw.tool.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

public class WXEntryActivity extends MyBaseActivity implements IWXAPIEventHandler {

    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    private Handler EBhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(WXEntryActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        initWx();
        inits();

    }

    private void initWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID_WX, false);
        api.handleIntent(getIntent(), this);
    }

    private void inits() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq baseReq) {
        System.out.print("onReq.getType()=" + baseReq.getType());
    }

    // 微信登陆Code
    String code;

    /**
     * sendToWx微信回调
     *
     * @param resp
     */
    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            String result = "";
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    result = "微信登陆成功";
                    code = ((SendAuth.Resp) resp).code;
                    getLocation();

//                    System.out.println("code=" + code);
//                    Toast.makeText(this, "code="+code, Toast.LENGTH_LONG).show();

                    break;

                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    result = "登陆取消";
                    break;

                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = "取消登陆";
                    break;

                default:
                    result = "登陆失败";
                    break;
            }
//            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            finish();

        } else if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {// 分享
            String result = "";
            System.out.println("resp.errCode=" + resp.errCode);
            switch (resp.errCode) {

                case BaseResp.ErrCode.ERR_OK:
                    result = "分享成功";
                    break;

                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = "分享取消";
                    break;

                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    result = "分享拒绝";
                    break;

                default:
                    result = "分享失败";
                    break;
            }
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

    }

    private void getLocation() {
        MapTool.getLocation(this, new MapTool.locationCallBack() {
            @Override
            public void onSuccess(String latitude, String longitude, String location, String province, String city, String district) {

                // 根据省市区查询站点
                getCity(province, city, district);
            }

            @Override
            public void onFailed(String tip) {
//                Toast.makeText(SignUpActivity.this, tip + ",请手动选择站点", Toast.LENGTH_SHORT).show();
                // 查询默认站点
                getCity("未知省", "未知市", "未知区");
            }
        });
    }

    private void getCity(String province, String city, String district) {
        JSONObject object = new JSONObject();
        try {
            object.put("status", "2");
            object.put("province", province.substring(0, province.length()-1));
            object.put("city", city.substring(0, province.length()-1));
            object.put("area", district);
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_806012, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Gson gson = new Gson();
                    CityModel model = gson.fromJson(jsonObject.toString(), new TypeToken<CityModel>() {
                    }.getType());

                    if (model != null) {
                        SignInToWx(model.getCode());
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(WXEntryActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(WXEntryActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void SignInToWx(final String companyCode) {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("mobile", wxMobileSp.getString("mobile",""));
            object.put("smsCaptcha", wxMobileSp.getString("smsCaptcha",""));
            object.put("isRegHx", "1");
            object.put("type", "4");
            object.put("companyCode", companyCode);
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_805151, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("isNeedMobile").equals("1")) { //需要绑定手机号码
                        Toast.makeText(WXEntryActivity.this, "请先填写手机号码", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(WXEntryActivity.this, BindPhoneActivity.class)
                                .putExtra("code",code)
                                .putExtra("companyCode",companyCode));

                    } else {
                        SharedPreferences.Editor editor = userInfoSp.edit();
                        editor.putString("userId", jsonObject.getString("userId"));
                        editor.putString("token", jsonObject.getString("token"));
                        editor.putString("mobile", "");
                        editor.putString("smsCaptcha", "");
                        editor.commit();

                        signInToEB();
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(WXEntryActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(WXEntryActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signInToEB() {
        EMClient.getInstance().login(userInfoSp.getString("userId", null), "123456", new EMCallBack() {
            @Override
            public void onSuccess() {
                finish();
                if(null != BindPhoneActivity.INSTANCE){
                    BindPhoneActivity.INSTANCE.finish();
                }
                startActivity(new Intent(WXEntryActivity.this, MainActivity.class));

            }

            @Override
            public void onError(int i, String s) {
                Message message = EBhandler.obtainMessage();
                message.obj = "登录失败: " + i + ", " + s;
                EBhandler.sendMessage(message);
                Log.e("EMClient_login", "登录失败 " + i + ", " + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

}