package com.zhejiangshegndian.csw.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.activity.PersonActivity;
import com.zhejiangshegndian.csw.adapter.SearchPeopleAdapter;
import com.zhejiangshegndian.csw.model.PersonalModel;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.DeviceTool;
import com.zhejiangshegndian.csw.tool.Xutil;
import com.zhejiangshegndian.csw.view.RefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by LeiQ on 2017/4/14.
 */

public class SearchPeopleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, AdapterView.OnItemClickListener {


    @InjectView(R.id.edt_search)
    EditText edtSearch;
    @InjectView(R.id.layout_delete)
    LinearLayout layoutDelete;
    @InjectView(R.id.list_people)
    ListView listPeople;
    @InjectView(R.id.layout_refresh)
    RefreshLayout layoutRefresh;

    private View view;

    private String searchContent;

    private int page = 1;
    private int pageSize = 10;
    private List<PersonalModel> list;
    private SearchPeopleAdapter adapter;

    private SharedPreferences citySp;
    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_people, null);
        ButterKnife.inject(this, view);

        inits();
        initEditText();
        initRefreshLayout();

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void inits() {
        list = new ArrayList<>();

        citySp = getActivity().getSharedPreferences("city", Context.MODE_PRIVATE);
        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getActivity().getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    private void initEditText() {
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.i("SEARCH_INPUT", edtSearch.getText().toString());
                    DeviceTool.hideKeyword(getActivity());
                    searchContent = edtSearch.getText().toString();

                    if (searchContent.equals("")) {
                        Toast.makeText(getActivity(), "请输入用户名称", Toast.LENGTH_SHORT).show();
                    } else {
                        getPeople();
                    }
                }
                return false;
            }
        });
    }

    private void initListView() {
        adapter = new SearchPeopleAdapter(getActivity(), list);
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

    @OnClick(R.id.layout_delete)
    public void onClick() {
        edtSearch.setText("");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(list.size() > 0){
            startActivity(new Intent(getActivity(), PersonActivity.class).putExtra("userId",list.get(i).getUserId()));
        }
    }

    private void getPeople() {
        JSONObject object = new JSONObject();
        try {
            object.put("nickname", searchContent);
            object.put("kind", "f1");
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("companyCode", citySp.getString("cityCode", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_805255, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    List<PersonalModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<PersonalModel>>() {
                    }.getType());

                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(lists);
                    initListView();

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


}
