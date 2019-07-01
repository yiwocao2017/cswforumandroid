package com.zhejiangshegndian.csw.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiniu.android.http.ResponseInfo;
import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.model.PersonalModel;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.ImageTool;
import com.zhejiangshegndian.csw.tool.QiNiuUtil;
import com.zhejiangshegndian.csw.tool.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhejiangshegndian.csw.tool.Constants.CODE_805077;
import static com.zhejiangshegndian.csw.tool.Constants.CODE_805156;
import static com.zhejiangshegndian.csw.tool.ImageTool.RESULT_CAMARA_IMAGE;
import static com.zhejiangshegndian.csw.tool.ImageTool.RESULT_LOAD_IMAGE;
import static com.zhejiangshegndian.csw.tool.ImageTool.album;
import static com.zhejiangshegndian.csw.tool.ImageTool.camara;

public class EditActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_confirm)
    TextView txtConfirm;
    @InjectView(R.id.img_photo)
    ImageView imgPhoto;
    @InjectView(R.id.edt_name)
    EditText edtName;
    @InjectView(R.id.txt_age)
    TextView txtAge;
    @InjectView(R.id.txt_gender)
    TextView txtGender;
    @InjectView(R.id.edt_email)
    EditText edtEmail;
    @InjectView(R.id.edt_introduce)
    EditText edtIntroduce;
    @InjectView(R.id.layout_photo)
    LinearLayout layoutPhoto;

    private Calendar calendar;

    private PersonalModel model;

    // 性别
    final int[] gander = {0};
    final String[] ganders = {"女", "男"};
    // 头像URL
    String photoUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.inject(this);

        inits();
        getData();
    }

    private void inits() {
        calendar = Calendar.getInstance();
    }

    @OnClick({R.id.layout_back, R.id.txt_confirm, R.id.layout_photo, R.id.txt_age, R.id.txt_gender})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_confirm:
                if (check()) {
                    updatePhoto(photoUrl);
                }
                break;

            case R.id.layout_photo:
                choosePhoto(view);
                break;

            case R.id.txt_age:
                new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
//
                        if ((month + 1) < 10) {
                            txtAge.setText(year + "-0" + (month + 1) + "-" + day);
                        } else {
                            txtAge.setText(year + "-" + (month + 1) + "-" + day);
                        }

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.txt_gender:
                showGender(view);
                break;
        }
    }

    private void choosePhoto(View view) {

        // 一个自定义的布局，作为显示的内容
        View mview = LayoutInflater.from(this).inflate(
                R.layout.popup_release, null);

        TextView txtPhotograph = (TextView) mview
                .findViewById(R.id.txt_photograph);
        TextView txtAlbum = (TextView) mview
                .findViewById(R.id.txt_album);
        TextView txtCancel = (TextView) mview
                .findViewById(R.id.txt_releasePopup_cancel);

        LinearLayout dismiss = (LinearLayout) mview.findViewById(R.id.quxiao);

        final PopupWindow popupWindow = new PopupWindow(mview, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, false);

        popupWindow.setTouchable(true);

        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        txtAlbum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // 调用android的图库
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
                popupWindow.dismiss();
            }
        });

        txtPhotograph.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_CAMARA_IMAGE);

                popupWindow.dismiss();
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.corners_share));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, Gravity.BOTTOM);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //data为B中回传的Intent
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                if (data.getData() != null) {
                    Glide.with(EditActivity.this).load(album(EditActivity.this, data)).into(imgPhoto);

                    new QiNiuUtil(EditActivity.this, album(EditActivity.this, data), null).qiNiu(new QiNiuUtil.QiNiuCallBack() {
                        @Override
                        public void onSuccess(String key, ResponseInfo info, JSONObject res) {
//                            updatePhoto(key);
                            photoUrl = key;
                            model.getUserExt().setPhoto(photoUrl);
                        }
                    }, true);
                }

            } else if (requestCode == RESULT_CAMARA_IMAGE) {
                if (data.getExtras() != null) {
                    Glide.with(EditActivity.this).load(camara(EditActivity.this, data)).into(imgPhoto);

                    new QiNiuUtil(EditActivity.this, camara(EditActivity.this, data), null).qiNiu(new QiNiuUtil.QiNiuCallBack() {
                        @Override
                        public void onSuccess(String key, ResponseInfo info, JSONObject res) {
//                            updatePhoto(key);
                            photoUrl = key;
                            model.getUserExt().setPhoto(photoUrl);
                        }
                    }, true);
                }

            }

        }
    }

    private void getData() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_805256, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<PersonalModel>() {
                    }.getType());

                    setView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(EditActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(EditActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView() {
        if (!model.getUserExt().getPhoto().equals("")) {
            photoUrl = model.getUserExt().getPhoto();
            ImageTool.photo(EditActivity.this, model.getUserExt().getPhoto(), imgPhoto);
        }
        edtName.setText(model.getNickname());
        if(model.getUserExt().getBirthday() != null){
            txtAge.setText(model.getUserExt().getBirthday());
        }
        if(model.getUserExt().getGender() != null){
            txtGender.setText(ganders[Integer.parseInt(model.getUserExt().getGender())]);
        }
        if(model.getUserExt().getEmail() != null){
            edtEmail.setText(model.getUserExt().getEmail());
        }
        if(model.getUserExt().getIntroduce() != null){
            edtIntroduce.setText(model.getUserExt().getIntroduce());
        }

    }

    private void showGender(View view) {

        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(this).inflate(R.layout.popup_gender, null);

        final NumberPicker pickerGander = (NumberPicker) mView.findViewById(R.id.picker_gander);
        TextView txtCancel = (TextView) mView.findViewById(R.id.txt_cancel);
        TextView txtConfirm = (TextView) mView.findViewById(R.id.txt_confirm);

        final PopupWindow popupWindow = new PopupWindow(mView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setTouchable(true);
//        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });


        pickerGander.setDisplayedValues(ganders);
        pickerGander.setMinValue(0);
        pickerGander.setMaxValue(ganders.length - 1);
        pickerGander.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                gander[0] = i1;

            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
            }
        });

        txtConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                txtGender.setText(ganders[gander[0]]);
                popupWindow.dismiss();
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_share));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    private void updatePhoto(final String url) {
        JSONObject object = new JSONObject();
        try {
            object.put("photo", url);
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_805077, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                model.getUserExt().setPhoto(url);
                modifyData();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(EditActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(EditActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void modifyData() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", ""));
            object.put("token", userInfoSp.getString("token", ""));
            object.put("nickName", edtName.getText().toString().trim());
            object.put("gender", gander[0]);
            object.put("birthday", txtAge.getText().toString().trim());
            object.put("email", edtEmail.getText().toString().trim());
            object.put("introduce", edtIntroduce.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_805156, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(EditActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(EditActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(EditActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean check() {
        if (model.getUserExt().getPhoto().equals("")) {
            Toast.makeText(this, "请选择头像", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtName.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请填写昵称", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtAge.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请选择生日", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtGender.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请选择性别", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtEmail.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请输入邮箱", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
