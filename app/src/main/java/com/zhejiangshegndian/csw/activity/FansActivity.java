package com.zhejiangshegndian.csw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.adapter.FansAdapter;
import com.zhejiangshegndian.csw.model.AttentionModel;
import com.zhejiangshegndian.csw.tool.Constants;
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

public class FansActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_fans)
    ListView listFans;
    @InjectView(R.id.layout_refresh)
    RefreshLayout layoutRefresh;

    private int page = 1;
    private int pageSize = 10;
    private List<AttentionModel> list;
    private FansAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans);
        ButterKnife.inject(this);
        inits();
        initListView();
        initRefreshLayout();

        getPeople();
    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new FansAdapter(this, list);
    }

    private void initListView() {
        listFans.setOnItemClickListener(this);
        listFans.setAdapter(adapter);
    }

    private void initRefreshLayout() {
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        layoutRefresh.setOnRefreshListener(this);

    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(list.size() > 0){
            startActivity(new Intent(FansActivity.this, PersonActivity.class).putExtra("userId", list.get(i).getUserId()));
        }
    }

    private void getPeople() {
        JSONObject object = new JSONObject();
        try {
            // 我关注了谁
            object.put("userId", "");
            object.put("mobile", "");
            // 谁关注了这个人
            object.put("toUser", userInfoSp.getString("userId", ""));
            object.put("token", userInfoSp.getString("token", ""));
            object.put("systemCode", appConfigSp.getString("systemCode", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post(Constants.CODE_805091, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONArray jsonArray = new JSONArray(result);

                    Gson gson = new Gson();
                    ArrayList<AttentionModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<AttentionModel>>() {
                    }.getType());

                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(lists);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(FansActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(FansActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onRefresh() {
        layoutRefresh.postDelayed(new Runnable() {

            @Override
            public void run() {
                layoutRefresh.setRefreshing(false);
                page = 1;
                getPeople();
                // 更新数据
                // 更新完后调用该方法结束刷新

            }

        }, 1500);
    }


}
