package com.zhejiangshegndian.csw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.model.GoodsModel;
import com.zhejiangshegndian.csw.tool.ImageTool;
import com.zhejiangshegndian.csw.tool.MoneyUtil;
import com.zhejiangshegndian.csw.tool.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhejiangshegndian.csw.tool.Constants.CODE_806010;
import static com.zhejiangshegndian.csw.tool.Constants.CODE_808006;
import static com.zhejiangshegndian.csw.tool.Constants.CODE_808026;
import static com.zhejiangshegndian.csw.tool.Constants.CODE_808050;

public class GoodActivity extends MyBaseActivity {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.img_photo)
    ImageView imgPhoto;
    @InjectView(R.id.txt_name)
    TextView txtName;
    @InjectView(R.id.txt_type)
    TextView txtType;
    @InjectView(R.id.txt_address)
    TextView txtAddress;
    @InjectView(R.id.txt_price)
    TextView txtPrice;
    @InjectView(R.id.txt_confirm)
    TextView txtConfirm;
    private String code;

    private GoodsModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good);
        ButterKnife.inject(this);
        inits();
        getDatas();
        getAddress();
    }

    private void inits() {
        code = getIntent().getStringExtra("code");

    }

    @OnClick({R.id.layout_back, R.id.txt_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_confirm:
                buyNow();
                break;
        }
    }

    /**
     * 获取商品详情
     */
    public void getDatas() {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808026, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<GoodsModel>() {
                    }.getType());

                    getType(model.getType());
                    setView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(GoodActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(GoodActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setView() {
        ImageTool.glide(GoodActivity.this, model.getAdvPic().split("\\|\\|")[0], imgPhoto);

        txtName.setText(model.getName());
        txtPrice.setText(MoneyUtil.moneyFormatDouble(model.getPrice2())+"赏金");

    }

    public void getAddress() {
        JSONObject object = new JSONObject();
        try {
            object.put("code", citySp.getString("cityCode",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_806010, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    String province = jsonObject.getString("province");
                    String city = jsonObject.getString("city");
                    String area = jsonObject.getString("area");
                    String address = jsonObject.getString("address");

                    txtAddress.setText(province + city + area + address);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(GoodActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(GoodActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getType(String code) {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808006, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String name = jsonObject.getString("name");
                    txtType.setText(name);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(GoodActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(GoodActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void buyNow() {

        JSONObject pojo = new JSONObject();
        try {
            pojo.put("receiver", userInfoSp.getString("nickName","receiver"));
            pojo.put("reMobile", userInfoSp.getString("mobile","reMobile"));
            pojo.put("reAddress", userInfoSp.getString("reAddress","reAddress"));
            pojo.put("applyUser", userInfoSp.getString("userId", null));
            pojo.put("applyNote", "");
            pojo.put("receiptType", "");
            pojo.put("receiptTitle", "");
            pojo.put("systemCode", appConfigSp.getString("systemCode", null));
            pojo.put("companyCode", citySp.getString("cityCode",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject object = new JSONObject();
        try {
            object.put("productCode", model.getCode());
            object.put("toUser", "");
            object.put("quantity", "1");
            object.put("pojo", pojo);
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808050, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {

                    startActivity(new Intent(GoodActivity.this, PayActivity.class)
                            .putExtra("price", txtPrice.getText().toString().trim())
                            .putExtra("code", result));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(GoodActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(GoodActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
