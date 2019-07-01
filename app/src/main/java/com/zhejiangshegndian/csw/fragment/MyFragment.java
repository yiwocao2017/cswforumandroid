package com.zhejiangshegndian.csw.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.activity.AttentionActivity;
import com.zhejiangshegndian.csw.activity.AwardActivity;
import com.zhejiangshegndian.csw.activity.CollectActivity;
import com.zhejiangshegndian.csw.activity.DraftActivity;
import com.zhejiangshegndian.csw.activity.FansActivity;
import com.zhejiangshegndian.csw.activity.MainActivity;
import com.zhejiangshegndian.csw.activity.MessageActivity;
import com.zhejiangshegndian.csw.activity.MyGoodActivity;
import com.zhejiangshegndian.csw.activity.MyNoteActivity;
import com.zhejiangshegndian.csw.activity.PersonActivity;
import com.zhejiangshegndian.csw.activity.RichTextActivity;
import com.zhejiangshegndian.csw.activity.SettingActivity;
import com.zhejiangshegndian.csw.model.AccountModel;
import com.zhejiangshegndian.csw.model.PersonalModel;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.ImageTool;
import com.zhejiangshegndian.csw.tool.MoneyUtil;
import com.zhejiangshegndian.csw.tool.SystemTool;
import com.zhejiangshegndian.csw.tool.Xutil;
import com.zhejiangshegndian.csw.view.RefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by LeiQ on 2017//3.
 */

public class MyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.txt_title)
    TextView txtTitle;
    @InjectView(R.id.img_photo)
    CircleImageView imgPhoto;
    @InjectView(R.id.txt_name)
    TextView txtName;
    @InjectView(R.id.txt_level)
    TextView txtLevel;
    @InjectView(R.id.layout_userInfo)
    LinearLayout layoutUserInfo;
    @InjectView(R.id.txt_note)
    TextView txtNote;
    @InjectView(R.id.layout_note)
    LinearLayout layoutNote;
    @InjectView(R.id.txt_attention)
    TextView txtAttention;
    @InjectView(R.id.layout_attention)
    LinearLayout layoutAttention;
    @InjectView(R.id.txt_funs)
    TextView txtFuns;
    @InjectView(R.id.layout_funs)
    LinearLayout layoutFuns;
    @InjectView(R.id.txt_award)
    TextView txtAward;
    @InjectView(R.id.layout_award)
    LinearLayout layoutAward;
    @InjectView(R.id.layout_goods)
    LinearLayout layoutGoods;
    @InjectView(R.id.layout_collect)
    LinearLayout layoutCollect;
    @InjectView(R.id.layout_edit)
    LinearLayout layoutEdit;
    @InjectView(R.id.layout_message)
    LinearLayout layoutMessage;
    @InjectView(R.id.layout_setting)
    LinearLayout layoutSetting;
    @InjectView(R.id.layout_about)
    LinearLayout layoutAbout;
    @InjectView(R.id.layout_refresh)
    RefreshLayout layoutRefresh;
    @InjectView(R.id.txt_unread)
    TextView txtUnread;

    private View view;

    private PersonalModel model;

    private SharedPreferences citySp;
    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    private List<AccountModel> list;

    MainActivity activity;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                txtUnread.setVisibility(View.GONE);
            }else{
                txtUnread.setVisibility(View.VISIBLE);
                txtUnread.setText(msg.what+"");
            }
        }

    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my, null);
        ButterKnife.inject(this, view);


        inits();
        initRefreshLayout();

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getData();
            getAccount();
            getNoteSize();
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();

        getData();
        getAccount();
        getNoteSize();

        showUnread(activity.unreadNum);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        // 移除环信聊天消息监听
    }

    private void inits() {
        activity = (MainActivity) getActivity();

        list = new ArrayList<>();

        citySp = getActivity().getSharedPreferences("city", Context.MODE_PRIVATE);
        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getActivity().getSharedPreferences("appConfig", Context.MODE_PRIVATE);

    }

    private void initRefreshLayout() {
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        layoutRefresh.setOnRefreshListener(this);
    }

    @OnClick({R.id.layout_userInfo, R.id.layout_note, R.id.layout_attention, R.id.layout_funs,
            R.id.layout_award, R.id.layout_goods, R.id.layout_collect, R.id.layout_edit,
            R.id.layout_message, R.id.layout_setting, R.id.layout_about})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_userInfo:
                startActivity(new Intent(getActivity(), PersonActivity.class)
                        .putExtra("userId", userInfoSp.getString("userId", "")));
                break;

            case R.id.layout_note:
                startActivity(new Intent(getActivity(), MyNoteActivity.class));
                break;

            case R.id.layout_attention:
                startActivity(new Intent(getActivity(), AttentionActivity.class));
                break;

            case R.id.layout_funs:
                startActivity(new Intent(getActivity(), FansActivity.class));
                break;

            case R.id.layout_award:
                startActivity(new Intent(getActivity(), AwardActivity.class));
                break;

            case R.id.layout_goods:
                startActivity(new Intent(getActivity(), MyGoodActivity.class));
                break;

            case R.id.layout_collect:
                startActivity(new Intent(getActivity(), CollectActivity.class));
                break;

            case R.id.layout_edit:
                startActivity(new Intent(getActivity(), DraftActivity.class));
                break;

            case R.id.layout_message:
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;

            case R.id.layout_about:
                startActivity(new Intent(getActivity(), RichTextActivity.class).putExtra("cKey", "cswDescription"));
                break;

            case R.id.layout_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
        }
    }

    /**
     * 获取用户详情
     */
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

                    SharedPreferences.Editor editor = userInfoSp.edit();
//                    editor.putString("mobile", model.getMobile());
                    editor.putString("photo", model.getUserExt().getPhoto());
                    editor.putString("nickName", model.getNickname());
                    editor.commit();

                    setView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(getActivity(), "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView() {
        ImageTool.photo(getActivity(), model.getUserExt().getPhoto(), imgPhoto);
        txtName.setText(model.getNickname());
        txtLevel.setText(SystemTool.levelFormat(model.getLevel()));

        txtFuns.setText(model.getTotalFansNum());
        txtAttention.setText(model.getTotalFollowNum());
    }

    private void getNoteSize() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", ""));
//            object.put("companyCode", citySp.getString("cityCode",null));
            object.put("status", "BD");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_610150, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                txtNote.setText(result.split("\\.")[0]);

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(getActivity(), "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAccount() {
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_802503, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    Gson gson = new Gson();
                    List<AccountModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<AccountModel>>() {
                    }.getType());

                    for (AccountModel model : lists) {
                        if (model.getCurrency().equals("JF")) {
                            txtAward.setText(MoneyUtil.moneyFormatDouble(model.getAmount()));

                            SharedPreferences.Editor editor = userInfoSp.edit();
                            editor.putString("accountNumber", model.getAccountNumber());
                            editor.commit();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(getActivity(), "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showUnread(int unreadNum){

        Message message = new Message();
        message.what = unreadNum;
        handler.sendMessage(message);
    }

    @Override
    public void onRefresh() {
        layoutRefresh.postDelayed(new Runnable() {

            @Override
            public void run() {
                layoutRefresh.setRefreshing(false);
                getData();
                getAccount();
                getNoteSize();

            }
        }, 1500);
    }
}
