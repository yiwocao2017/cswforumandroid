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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.activity.WebActivity;
import com.zhejiangshegndian.csw.adapter.VideoAdapter;
import com.zhejiangshegndian.csw.model.VideoModel;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.Xutil;
import com.zhejiangshegndian.csw.view.RefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by LeiQ on 2017//3.
 */

public class VideoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, AdapterView.OnItemClickListener {

    @InjectView(R.id.txt_title)
    TextView txtTitle;
    @InjectView(R.id.grid_video)
    GridView griVideo;
    @InjectView(R.id.swipe_container)
    RefreshLayout layoutRefresh;

    private View view;

    private int page = 1;
    private int pageSize = 10;
    private List<VideoModel> list;
    private VideoAdapter adapter;

    private SharedPreferences citySp;
    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video, null);
        ButterKnife.inject(this, view);

        inits();
        initListView();
        initRefreshLayout();

        getVideo();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            getVideo();
        }
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

    private void initListView() {
        adapter = new VideoAdapter(getActivity(), list);
        griVideo.setAdapter(adapter);
        griVideo.setOnItemClickListener(this);
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
            startActivity(new Intent(getActivity(), WebActivity.class).putExtra("url", list.get(i).getUrl()));
        }
    }

    private void getVideo() {
        JSONObject object = new JSONObject();
        try {
            object.put("name", "");
            object.put("status", "2");
            object.put("parentCode", "");
            object.put("companyCode", citySp.getString("cityCode", null));
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("orderColumn", "order_no");
            object.put("orderDir", "asc");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_610055, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    List<VideoModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<VideoModel>>() {
                    }.getType());


                    if (page == 1) {
                        list.clear();
                    }
                    if(lists.size() == 1){
                        startActivity(new Intent(getActivity(), WebActivity.class)
                                .putExtra("url",lists.get(0).getUrl()));
                    }else{
                        list.addAll(lists);
                        adapter.notifyDataSetChanged();
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

    @Override
    public void onRefresh() {
        layoutRefresh.postDelayed(new Runnable() {

            @Override
            public void run() {
                layoutRefresh.setRefreshing(false);
                page = 1;
                getVideo();
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
                getVideo();
            }
        }, 1500);
    }
}
