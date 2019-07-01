package com.zhejiangshegndian.csw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.adapter.AwardAdapter;
import com.zhejiangshegndian.csw.model.AwardModel;
import com.zhejiangshegndian.csw.tool.Xutil;
import com.zhejiangshegndian.csw.view.RefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhejiangshegndian.csw.tool.Constants.CODE_802524;

public class AwardActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.layout_mall)
    LinearLayout layoutMall;
    @InjectView(R.id.layout_how)
    LinearLayout layoutHow;
    @InjectView(R.id.list_award)
    ListView listAward;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;
    private int page = 1;
    private int pageSize = 10;
    private List<AwardModel> list;
    private AwardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);
        ButterKnife.inject(this);

        inits();
        initListView();
        initRefreshLayout();

        getAward();
    }

    public void inits() {
        list = new ArrayList<>();
        adapter = new AwardAdapter(AwardActivity.this, list);
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setOnLoadListener(this);
    }

    private void initListView() {
        listAward.setAdapter(adapter);
    }

    @OnClick({R.id.layout_back, R.id.layout_mall, R.id.layout_how})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_mall:
                startActivity(new Intent(AwardActivity.this, StoreActivity.class));
                break;

            case R.id.layout_how:
                startActivity(new Intent(AwardActivity.this, RichTextActivity.class).putExtra("cKey","cswReward"));
                break;
        }
    }

    private void getAward() {
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("accountNumber", userInfoSp.getString("accountNumber", null));
            object.put("status", "");
            object.put("start", page);
            object.put("limit", pageSize);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_802524, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    List<AwardModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<AwardModel>>() {
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
                Toast.makeText(AwardActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(AwardActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
                page = 1;
                getAward();
                // 更新数据
                // 更新完后调用该方法结束刷新
            }
        }, 1500);
    }

    @Override
    public void onLoad() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setLoading(false);
                page = page + 1;
                getAward();
            }
        }, 1500);
    }


}
