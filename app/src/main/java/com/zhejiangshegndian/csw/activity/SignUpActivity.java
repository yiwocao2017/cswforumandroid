package com.zhejiangshegndian.csw.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.model.CityModel;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.MapTool;
import com.zhejiangshegndian.csw.tool.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * @author leiQ
 * @version 注册
 */
public class SignUpActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.edt_user)
    EditText edtUser;
    @InjectView(R.id.edt_code)
    EditText edtCode;
    @InjectView(R.id.btn_send)
    TextView btnSend;
    @InjectView(R.id.edt_pwd)
    EditText edtPwd;
    @InjectView(R.id.edt_rePwd)
    EditText edtRePwd;
    @InjectView(R.id.txt_city)
    TextView txtCity;
    @InjectView(R.id.layout_city)
    LinearLayout layoutCity;
    @InjectView(R.id.txt_explain)
    TextView txtExplain;
    @InjectView(R.id.txt_signUp)
    TextView txtSignUp;
    @InjectView(R.id.txt_deal)
    TextView txtDeal;

    // 站点名称
    private String cityName;
    // 站点编号
    private String cityCode;

    private int i = 60;
    private Timer timer;
    private TimerTask task;

    // 验证码是否已发送 未发送false 已发送true
    private boolean isCodeSended = false;

    private Handler EBhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(SignUpActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
            super.handleMessage(msg);
        }
    };

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            btnSend.setText(i + "秒后重发");
            if (msg.arg1 == 0) {
                stopTime();
            } else {
                startTime();
            }
            super.handleMessage(msg);
        }
    };

    private Set<String> tags;
    private String alias = "";

    private static final int MSG_SET_ALIAS = 1001;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            // 调用 JPush 接口来设置别名。
            JPushInterface.setAliasAndTags(getApplicationContext(),
                    alias,
                    tags,
                    mAliasCallback);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.inject(this);

        inits();
        // 初始化站点
        initCity();
    }

    public void inits() {
        txtExplain.setText(Html.fromHtml("注册成功后将生成一个登陆名，格式为<html><font color=\"#d23e3e\">CSW + 手机号</font></html>,您可以使用这个登录名进行登陆。"));

        tags = new HashSet<>();
        tags.add(cityCode) ;
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
//                Toast.makeText(SignUpActivity.this, tip + ",请手动选择站点", Toast.LENGTH_SHORT).show();
                txtCity.setText(tip + ",请手动选择站点");
            }
        });
    }

    @OnClick({R.id.layout_back, R.id.btn_send, R.id.layout_city, R.id.txt_signUp, R.id.txt_deal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.btn_send:
                if (isCodeSended) {
                    Toast.makeText(SignUpActivity.this, "验证码每60秒发送发送一次", Toast.LENGTH_SHORT).show();
                } else {
                    if (edtUser.getText().toString().trim().length() != 11) {
                        Toast.makeText(SignUpActivity.this, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sendCode();
                }
                break;

            case R.id.layout_city:
                startActivityForResult(new Intent(SignUpActivity.this, CityActivity.class)
                        .putExtra("cityName",cityName)
                        .putExtra("cityCode",cityCode),0);
                break;

            case R.id.txt_signUp:
                if (check()) {
                    register();
                }
                break;

            case R.id.txt_deal:
                startActivity(new Intent(SignUpActivity.this, RichTextActivity.class).putExtra("cKey","cswRule"));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){
            cityName = data.getStringExtra("cityName");
            cityCode = data.getStringExtra("cityCode");

            txtCity.setText(cityName);
        }
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
                    CityModel model = gson.fromJson(jsonObject.toString(),new TypeToken<CityModel>(){}.getType());

                    if(model != null){
                        cityName = model.getName();
                        cityCode = model.getCode();
                    }

                    setCity();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(SignUpActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(SignUpActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setCity() {
        txtCity.setText(cityName);

    }

    private void sendCode() {
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", edtUser.getText().toString().trim());
            object.put("bizType", Constants.CODE_805076);
            object.put("kind", "f1");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_805904, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                isCodeSended = true;
                startTime();
                Toast.makeText(SignUpActivity.this, "短信已发送，请注意查收", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(SignUpActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(SignUpActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 验证码发送倒计时
     */
    private void startTime() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                i--;
                Message message = handler.obtainMessage();
                message.arg1 = i;
                handler.sendMessage(message);
            }

        };

        timer.schedule(task, 1000);
    }

    private void stopTime() {
        isCodeSended = false;
        i = 60;
        btnSend.setText("重新发送");
        timer.cancel();
    }

    private void register() {
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", edtUser.getText().toString().trim());
            object.put("loginPwd", edtPwd.getText().toString().trim());
            object.put("smsCaptcha", edtCode.getText().toString().trim());
            object.put("loginPwdStrength", "1");
            object.put("kind", "f1");
            object.put("isRegHx", "1");
            object.put("isMall", "1");
            object.put("companyCode", cityCode);
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_805076, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                SharedPreferences.Editor editor = userInfoSp.edit();
                editor.putString("mobile", edtUser.getText().toString().trim());
                editor.commit();

                signIn();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(SignUpActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(SignUpActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void signIn() {
        JSONObject object = new JSONObject();
        try {
            object.put("loginName", edtUser.getText().toString().trim());
            object.put("loginPwd", edtPwd.getText().toString().trim());
            object.put("kind", "f1");
            object.put("companyCode", "");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_805043, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    SharedPreferences.Editor editor = userInfoSp.edit();
                    editor.putString("userId", jsonObject.getString("userId"));
                    editor.putString("token", jsonObject.getString("token"));
                    editor.commit();

                    alias = jsonObject.getString("userId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                signInToEB();

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(SignUpActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(SignUpActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signInToEB() {
        EMClient.getInstance().login(userInfoSp.getString("userId", null), "123456", new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                mHandler.sendEmptyMessage(0);

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

    private boolean check() {
        if (edtUser.getText().toString().trim().length() != 11) {
            Toast.makeText(SignUpActivity.this, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtCode.getText().toString().trim().length() != 4) {
            Toast.makeText(SignUpActivity.this, "请填写正确的验证码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtPwd.getText().toString().trim().length() < 6) {
            Toast.makeText(SignUpActivity.this, "密码不能小于6位", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!edtPwd.getText().toString().trim().equals(edtRePwd.getText().toString().trim())) {
            Toast.makeText(SignUpActivity.this, "两次输入的密码不相同", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i("JPUSH", logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。

                    finish();
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i("JPUSH", logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("JPUSH", logs);
            }
        }
    };

}
