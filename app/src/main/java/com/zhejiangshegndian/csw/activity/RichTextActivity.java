package com.zhejiangshegndian.csw.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.tool.Xutil;
import com.zzhoujay.richtext.RichText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhejiangshegndian.csw.tool.Constants.CODE_807717;

public class RichTextActivity extends MyBaseActivity {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_about)
    TextView txtAbout;
    @InjectView(R.id.txt_title)
    TextView txtTitle;

    private String cKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rich_text);
        ButterKnife.inject(this);

        inits();
        setTitle();
        getDatas();
    }

    private void inits() {
        cKey = getIntent().getStringExtra("cKey");

    }

    private void setTitle() {

        switch (cKey) {

            case "cswDescription":
                txtTitle.setText("城市网介绍");
                break;

            case "cswReward":
                txtTitle.setText("如何赚赏金");
                break;

            case "cswRule":
                txtTitle.setText("城市网服务协议");
                break;

        }

    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }

    public void getDatas() {
        JSONObject object = new JSONObject();
        try {
            object.put("ckey", cKey);
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_807717, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    RichText.from(jsonObject.getString("note")).into(txtAbout);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(RichTextActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(RichTextActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RichText.clear(RichTextActivity.this);
//        MyApplication.getInstance().removeActivity(this);
    }
}
