package com.zhejiangshegndian.csw.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.WxUtil;
import com.zhejiangshegndian.csw.tool.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class BindPhoneActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.edt_phone)
    EditText edtPhone;
    @InjectView(R.id.txt_confirm)
    TextView txtConfirm;
    @InjectView(R.id.edt_code)
    EditText edtCode;
    @InjectView(R.id.btn_send)
    TextView btnSend;

    private String code;
    private String companyCode;

    private int i = 60;
    private Timer timer;
    private TimerTask task;

    // 验证码是否已发送 未发送false 已发送true
    private boolean isCodeSended = false;

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

    public static BindPhoneActivity INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        ButterKnife.inject(this);

        inits();
    }

    private void inits() {
        INSTANCE = this;

        code = getIntent().getStringExtra("code");
        companyCode = getIntent().getStringExtra("companyCode");
    }

    @OnClick({R.id.layout_back, R.id.txt_confirm, R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_confirm:
                if (check()) {
                    if (WxUtil.check(this)) {
                        SharedPreferences.Editor editor = wxMobileSp.edit();
                        editor.putString("mobile", edtPhone.getText().toString().trim());
                        editor.putString("smsCaptcha", edtCode.getText().toString().trim());
                        editor.commit();

                        WxUtil.signinToWx();

                    }
                }
                break;

            case R.id.btn_send:
                if (isCodeSended) {
                    Toast.makeText(BindPhoneActivity.this, "验证码每60秒发送发送一次", Toast.LENGTH_SHORT).show();
                } else {
                    if (edtPhone.getText().toString().trim().length() != 11) {
                        Toast.makeText(BindPhoneActivity.this, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sendCode();
                }
                break;
        }
    }

    private boolean check() {

        if (edtPhone.getText().toString().equals("")) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtPhone.getText().toString().length() != 11) {
            Toast.makeText(this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtCode.getText().toString().equals("")){
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtCode.getText().toString().length() != 4) {
            Toast.makeText(this, "验证码格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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

    private void sendCode() {
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", edtPhone.getText().toString().trim());
            object.put("bizType", Constants.CODE_805151);
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
                Toast.makeText(BindPhoneActivity.this, "短信已发送，请注意查收", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(BindPhoneActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(BindPhoneActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
