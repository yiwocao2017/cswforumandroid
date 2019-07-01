package com.zhejiangshegndian.csw.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.WxUtil;
import com.zhejiangshegndian.csw.tool.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class SignInActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.edt_user)
    EditText edtUser;
    @InjectView(R.id.edt_pwd)
    EditText edtPwd;
    @InjectView(R.id.txt_signIn)
    TextView txtSignIn;
    @InjectView(R.id.txt_signUp)
    TextView txtSignUp;
    @InjectView(R.id.txt_find)
    TextView txtFind;
    @InjectView(R.id.img_wx)
    ImageView imgWx;

    public static SignInActivity INSTANCE;

    private boolean EMSignOut;

    private Handler EBhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(SignInActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
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
            System.out.println("alias="+alias);
            System.out.println("tags="+tags);

            JPushInterface.setAliasAndTags(getApplicationContext(),
                    alias,
                    tags,
                    mAliasCallback);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.inject(this);
        inits();
    }

    private void inits() {
        INSTANCE = this;

        EMSignOut = getIntent().getBooleanExtra("EMSignOut",false);

        tags = new HashSet<>();
        tags.add(citySp.getString("cityCode","")) ;
    }

    @OnClick({R.id.txt_signIn, R.id.txt_signUp, R.id.txt_find, R.id.img_wx})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_signIn:
                if (check()) {
                    signIn();
                }
                break;

            case R.id.txt_signUp:
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                break;

            case R.id.txt_find:
                startActivity(new Intent(SignInActivity.this, ForgetActivity.class));
                break;

            case R.id.img_wx:
                if (WxUtil.check(this)) {
                    WxUtil.signinToWx();
                }
                break;
        }
    }

    private boolean check() {
        if (edtUser.getText().toString().trim().length() != 11) {
            Toast.makeText(SignInActivity.this, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtPwd.getText().toString().trim().length() < 6) {
            Toast.makeText(SignInActivity.this, "密码不能小于6位", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    private void signIn() {
        JSONObject object = new JSONObject();
        try {
            object.put("loginName", edtUser.getText().toString().trim());
            object.put("loginPwd", edtPwd.getText().toString());
            object.put("kind", "f1");
            object.put("companyCode", "");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_805043, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                SharedPreferences.Editor editor = userInfoSp.edit();

                try {
                    JSONObject jsonObject = new JSONObject(result);
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
                Toast.makeText(SignInActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(SignInActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.layout_back)
    public void onClick() {
        if(!EMSignOut){
            finish();
        }else{
            Toast.makeText(INSTANCE, "登陆之后才能继续刚才的事情哦!", Toast.LENGTH_SHORT).show();
        }
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

                    SharedPreferences.Editor editor = userInfoSp.edit();
                    editor.putBoolean("tagAndAlias",false);
                    editor.commit();

                    finish();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            tip();
        }
        return false;
    }

    private void tip() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("您确定要退出城市网吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton("取消", null).show();
    }
}
