package com.zhejiangshegndian.csw.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.model.BannerModel;
import com.zhejiangshegndian.csw.model.CityModel;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.ImageTool;
import com.zhejiangshegndian.csw.tool.MapTool;
import com.zhejiangshegndian.csw.tool.PermissionTool;
import com.zhejiangshegndian.csw.tool.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.zhejiangshegndian.csw.tool.PermissionTool.PERMISSON_REQUESTCODE;
import static com.zhejiangshegndian.csw.tool.PermissionTool.verifyPermissions;

public class StartActivity extends Activity {

    @InjectView(R.id.img_startImg)
    ImageView imgStartImg;

    int time = 3000;

    private Timer timer;
    private TimerTask task;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1) {
                startApp();
            }

        }
    };

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    public SharedPreferences citySp;
    public SharedPreferences appConfigSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.inject(this);

        citySp = getSharedPreferences("city", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 请求权限
        if (isNeedCheck) {
            PermissionTool.permission(this, needPermissions).callback(new PermissionTool.PermissionCallback() {
                @Override
                public void onSuccess() {
                    initCity();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
        System.out.println("onRequestPermissionsResult()");
        if (requestCode == PERMISSON_REQUESTCODE) {
            isNeedCheck = false;
            if (verifyPermissions(paramArrayOfInt)) {
                initCity();
            } else {
                PermissionTool.showMissingPermissionDialog("位置", StartActivity.this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initCity() {
        MapTool.getLocation(this, new MapTool.locationCallBack() {
            @Override
            public void onSuccess(String latitude, String longitude, String location, String province, String city, String district) {

                // 根据省市区查询站点
                getCity(province, city, district);
            }

            @Override
            public void onFailed(String tip) {
                // 模拟省市区查询默认站点
                getCity("省", "市", "区");
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

                    SharedPreferences.Editor editor = citySp.edit();
                    editor.putString("cityName", model.getName());
                    editor.putString("cityCode", model.getCode());
                    editor.commit();


                    getStartImage();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(StartActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(StartActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getStartImage() {
        JSONObject object = new JSONObject();
        try {
            object.put("companyCode", citySp.getString("cityCode", ""));
            object.put("location", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_610107, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    Gson gson = new Gson();
                    List<BannerModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<BannerModel>>() {
                    }.getType());

                    for (BannerModel model : lists) {
                        if (model.getPic() != null) {
                            ImageTool.glide(StartActivity.this,model.getPic(),imgStartImg);
                        }
                    }
                    startTime();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(StartActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(StartActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startTime() {
        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                Message message = handler.obtainMessage();
                message.arg1 = 1;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, time);
    }

    private void stopTime() {
        timer.cancel();
    }

    private void startApp() {
        startActivity(new Intent(StartActivity.this, MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        stopTime();
        finish();
    }

}
