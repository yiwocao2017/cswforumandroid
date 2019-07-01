package com.zhejiangshegndian.csw.activity;

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
import com.zhejiangshegndian.csw.tool.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ModifyPhoneActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.edt_user)
    EditText edtUser;
    @InjectView(R.id.edt_code)
    EditText edtCode;
    @InjectView(R.id.btn_send)
    TextView btnSend;
    @InjectView(R.id.txt_confirm)
    TextView txtConfirm;

    // 验证码是否已发送 未发送false 已发送true
    private boolean isCodeSended = false;

    private int i = 60;
    private Timer timer;
    private TimerTask task;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.layout_back, R.id.btn_send, R.id.txt_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.btn_send:
                if (edtUser.getText().length() == 11) {
                    if (isCodeSended) {
                        Toast.makeText(ModifyPhoneActivity.this, "验证码每60秒发送发送一次", Toast.LENGTH_SHORT).show();
                    } else {
                        sendCode();
                    }
                } else {
                    Toast.makeText(ModifyPhoneActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.txt_confirm:
                if (edtUser.getText().length() != 11) {
                    Toast.makeText(ModifyPhoneActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtCode.getText().toString().trim().length() != 4) {
                    Toast.makeText(ModifyPhoneActivity.this, "请填写正确的验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                modifyPhone();
                break;
        }
    }

    private void modifyPhone() {
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("newMobile", edtUser.getText().toString().trim());
            object.put("smsCaptcha", edtCode.getText().toString().trim());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_805047, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(ModifyPhoneActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ModifyPhoneActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ModifyPhoneActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendCode() {
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", edtUser.getText().toString().trim());
            object.put("bizType", Constants.CODE_805047);
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
                Toast.makeText(ModifyPhoneActivity.this, "短信已发送，请注意查收", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ModifyPhoneActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ModifyPhoneActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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
}
