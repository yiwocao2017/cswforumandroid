package com.zhejiangshegndian.csw.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.tool.CacheUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashSet;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static com.zhejiangshegndian.csw.tool.Constants.PORT;
import static com.zhejiangshegndian.csw.tool.Constants.SIGNOUT;
import static com.zhejiangshegndian.csw.tool.Constants.URL;

public class SettingActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_title)
    TextView txtTitle;
    @InjectView(R.id.layout_name)
    LinearLayout layoutName;
    @InjectView(R.id.layout_password)
    LinearLayout layoutPassword;
    @InjectView(R.id.layout_phone)
    LinearLayout layoutPhone;
    @InjectView(R.id.layout_personal)
    LinearLayout layoutPersonal;
    @InjectView(R.id.txt_cache)
    TextView txtCache;
    @InjectView(R.id.layout_cache)
    LinearLayout layoutCache;
    @InjectView(R.id.txt_signOut)
    TextView txtSignOut;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(SettingActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();

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
                    null,
                    tags,
                    mAliasCallback);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.inject(this);

        init();
    }

    private void init() {
        tags = new HashSet<>();
        tags.add(citySp.getString("cityCode","")) ;

        try {
            txtCache.setText(CacheUtil.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.layout_back, R.id.layout_name, R.id.layout_password, R.id.layout_phone,
            R.id.layout_personal, R.id.txt_cache, R.id.layout_cache, R.id.txt_signOut})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_name:
                startActivity(new Intent(SettingActivity.this, ModifyNameActivity.class));
                break;

            case R.id.layout_password:
                startActivity(new Intent(SettingActivity.this, ModifyPasswordActivity.class));
                break;

            case R.id.layout_phone:
                startActivity(new Intent(SettingActivity.this, ModifyPhoneActivity.class));
                break;

            case R.id.layout_personal:
                startActivity(new Intent(SettingActivity.this, PersonActivity.class)
                        .putExtra("userId",userInfoSp.getString("userId","")));
                break;

            case R.id.layout_cache:
                clearCache();
                break;

            case R.id.txt_signOut:
                signOutEM();
                break;
        }
    }

    private void clearCache() {
        CacheUtil.clearAllCache(this);
        try {
            txtCache.setText(CacheUtil.getTotalCacheSize(this));
            Toast.makeText(this, "缓存已清除", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void signOutEM() {

        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                signOut();
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                Message msg = handler.obtainMessage();
                msg.obj = "退出失败: " + code + ", " + message;
                handler.sendMessage(msg);

            }
        });
    }

    private void signOut(){
        RequestParams params = new RequestParams(URL + PORT + SIGNOUT);
        params.addBodyParameter("token", userInfoSp.getString("token", null));

        System.out.println("url="+URL + PORT + SIGNOUT);
        System.out.println("token="+userInfoSp.getString("token", null));

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getJSONObject("data").getBoolean("isSuccess")){
                        SharedPreferences.Editor editor = userInfoSp.edit();
                        editor.putString("userId", null);
                        editor.putString("token", null);
                        editor.commit();

                        mHandler.sendEmptyMessage(0);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
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

                    MainActivity.INSTANCE.finish();
                    finish();
                    startActivity(new Intent(SettingActivity.this, MainActivity.class));
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
