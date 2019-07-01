package com.zhejiangshegndian.csw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.adapter.LinkManAdapter;
import com.zhejiangshegndian.csw.model.AttentionModel;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.DeviceTool;
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

public class LinkManActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, AdapterView.OnItemClickListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.edt_search)
    EditText edtSearch;
    @InjectView(R.id.layout_delete)
    LinearLayout layoutDelete;
    @InjectView(R.id.list_people)
    ListView listPeople;
    @InjectView(R.id.layout_refresh)
    RefreshLayout layoutRefresh;

    private String searchContent;

    private int page = 1;
    private int pageSize = 10;
    private List<AttentionModel> list;
    private LinkManAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_man);
        ButterKnife.inject(this);

        inits();
        initEditText();
        initListView();
        initRefreshLayout();

        getPeople();
    }

    @OnClick({R.id.layout_back, R.id.layout_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                setResult(-1);
                finish();
                break;

            case R.id.layout_delete:
                break;
        }
    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new LinkManAdapter(this, list);

    }

    private void initEditText() {
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.i("SEARCH_INPUT", edtSearch.getText().toString());
                    DeviceTool.hideKeyword(LinkManActivity.this);
                    searchContent = edtSearch.getText().toString();

                    if(searchContent.equals("")){
                        Toast.makeText(LinkManActivity.this, "请输入用户名称", Toast.LENGTH_SHORT).show();
                    }else {
                        getPeople();
                    }
                }
                return false;
            }
        });
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(list.size() > 0){
            setResult(0,new Intent().putExtra("nickName",list.get(i).getNickname()));
            finish();
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

                    if(page ==1){
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
                Toast.makeText(LinkManActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(LinkManActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onLoad() {
        layoutRefresh.postDelayed(new Runnable() {

            @Override
            public void run() {
                layoutRefresh.setLoading(false);
                page = page + 1;
                getPeople();
            }
        }, 1500);
    }

    /**
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(-1);
            finish();
        }
        return false;
    }

}
