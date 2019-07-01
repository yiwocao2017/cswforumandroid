package com.zhejiangshegndian.csw.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.activity.GoodActivity;
import com.zhejiangshegndian.csw.adapter.OrderAdapter;
import com.zhejiangshegndian.csw.model.OrderModel;
import com.zhejiangshegndian.csw.tool.Xutil;
import com.zhejiangshegndian.csw.view.RefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.zhejiangshegndian.csw.tool.Constants.CODE_808068;

/**
 * Created by LeiQ on 2017//3.
 */

public class TakeFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {

    @InjectView(R.id.grid_store)
    GridView gridStore;
    @InjectView(R.id.layout_refresh)
    RefreshLayout layoutRefresh;

    private View view;

    private int page = 1;
    private int pageSize = 10;

    private List<OrderModel> list;
    private OrderAdapter adapter;

    private SharedPreferences citySp;
    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_take, null);
        ButterKnife.inject(this, view);
        inits();
        initGridView();
        initRefreshLayout();

        getOrder();

        return view;
    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new OrderAdapter(getActivity(), list);

        citySp = getActivity().getSharedPreferences("city", Context.MODE_PRIVATE);
        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getActivity().getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    private void initGridView() {
        gridStore.setOnItemClickListener(this);
        gridStore.setAdapter(adapter);
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
        startActivity(new Intent(getActivity(), GoodActivity.class).putExtra("code",
                list.get(i).getProductOrderList().get(0).getProductCode()));
    }

    private void getOrder() {
        JSONObject object = new JSONObject();
        try {
            object.put("applyUser", userInfoSp.getString("userId", null));
            object.put("mobile", "");
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("status", "4");
            object.put("token", userInfoSp.getString("token", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", citySp.getString("cityCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808068, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    ArrayList<OrderModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<OrderModel>>() {
                    }.getType());

                    if(page == 1){
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
                getOrder();
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
                getOrder();
            }
        }, 1500);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
