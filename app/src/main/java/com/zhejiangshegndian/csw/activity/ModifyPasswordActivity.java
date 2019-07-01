package com.zhejiangshegndian.csw.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.tool.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhejiangshegndian.csw.tool.Constants.CODE_805049;

public class ModifyPasswordActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_title)
    TextView txtTitle;
    @InjectView(R.id.edt_pwd_old)
    EditText edtPwdOld;
    @InjectView(R.id.edt_pwd_new)
    EditText edtPwdNew;
    @InjectView(R.id.edt_rePwd)
    EditText edtRePwd;
    @InjectView(R.id.txt_confirm)
    TextView txtConfirm;
    @InjectView(R.id.txt_phone)
    TextView txtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        ButterKnife.inject(this);

        init();
    }

    private void init() {
        txtPhone.setText(userInfoSp.getString("mobile","")+" 手机号");
    }

    @OnClick({R.id.layout_back, R.id.txt_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_confirm:
                if(check()){
                    modifyPassword();
                }
                break;
        }
    }

    private void modifyPassword() {
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("oldLoginPwd", edtPwdOld.getText().toString().trim());
            object.put("newLoginPwd", edtPwdNew.getText().toString().trim());
            object.put("loginPwdStrength", "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_805049, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(ModifyPasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ModifyPasswordActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ModifyPasswordActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean check(){
        if(edtPwdOld.getText().toString().trim().equals("")){
            Toast.makeText(this, "请输入原密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(edtPwdNew.getText().toString().trim().equals("")){
            Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(edtRePwd.getText().toString().trim().equals("")){
            Toast.makeText(this, "请再次输入新密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(edtPwdNew.getText().toString().trim().length()<6){
            Toast.makeText(this, "新密码为6-16位", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(edtPwdNew.getText().toString().trim().equals(edtRePwd.getText().toString().trim())){
            Toast.makeText(this, "两次输入的密码不相同", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
