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
import com.hyphenate.easeui.EaseConstant;
import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.adapter.ChatAdapter;
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

public class ChatListActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, AdapterView.OnItemClickListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_people)
    ListView listPeople;
    @InjectView(R.id.layout_refresh)
    RefreshLayout layoutRefresh;

    private List<AttentionModel> list;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        ButterKnife.inject(this);

        inits();
        initListView();
        initRefreshLayout();

        getPeople();
    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new ChatAdapter(this, list);

    }

    private void initListView() {
        listPeople.setOnItemClickListener(this);
        listPeople.setAdapter(adapter);
    }

    private void initRefreshLayout() {
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        layoutRefresh.setOnRefreshListener(this);
        layoutRefresh.setOnLoadListener(this);

    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(list.size() >0){
            Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
            intent.putExtra("nickName", list.get(i).getNickname());
            intent.putExtra("myPhoto", userInfoSp.getString("photo", ""));
            intent.putExtra("myName", userInfoSp.getString("nickName", ""));
            intent.putExtra("otherPhoto", list.get(i).getPhoto());
            intent.putExtra(EaseConstant.EXTRA_USER_ID, list.get(i).getUserId());
            intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
            startActivity(intent);
        }

    }

    private void getPeople() {
        JSONObject object = new JSONObject();
        try {
            // 我关注了谁
            object.put("userId", userInfoSp.getString("userId", ""));
            object.put("mobile", "");
            // 谁关注了这个人
            object.put("toUser", "");
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

                    list.clear();
                    list.addAll(lists);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ChatListActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ChatListActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onRefresh() {
        layoutRefresh.postDelayed(new Runnable() {

            @Override
            public void run() {
                layoutRefresh.setRefreshing(false);
                getPeople();
                // 更新数据
                // 更新完后调用该方法结束刷新

            }

        }, 1500);
    }

    @Override
    public void onLoad() {
        layoutRefresh.postDelayed(new Runnable() {

            @Override
            public void run() {
                layoutRefresh.setLoading(false);
                getPeople();
            }
        }, 1500);
    }
}
