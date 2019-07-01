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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.activity.NoteActivity;
import com.zhejiangshegndian.csw.adapter.YouLiaoAdapter;
import com.zhejiangshegndian.csw.model.NoteModel;
import com.zhejiangshegndian.csw.tool.Constants;
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
 * @author LeiQ
 * @version 有料Fragment
 */

public class ForumYLFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, AdapterView.OnItemClickListener {

    @InjectView(R.id.list_youliao)
    ListView listYouliao;
    @InjectView(R.id.layout_refresh)
    RefreshLayout layoutRefresh;
    @InjectView(R.id.txt_new)
    Button txtNew;
    @InjectView(R.id.txt_top)
    Button txtTop;
    @InjectView(R.id.txt_essence)
    Button txtEssence;

    private View view;

    private int page = 1;
    private int pageSize = 10;

    private String location = "";
    private List<NoteModel> list;
    private YouLiaoAdapter adapter;

    private SharedPreferences citySp;
    private SharedPreferences userInfoSp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_youliao, null);
        ButterKnife.inject(this, view);

        inits();
        initListView();
        initRefreshLayout();

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            getNote();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getNote();
    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new YouLiaoAdapter(getActivity(), list);
        citySp = getActivity().getSharedPreferences("city", Context.MODE_PRIVATE);
        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

    }

    private void initListView() {
        listYouliao.setAdapter(adapter);
        listYouliao.setOnItemClickListener(this);
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.txt_new, R.id.txt_top, R.id.txt_essence})
    public void onClick(View view) {
        initBtn();
        switch (view.getId()) {
            case R.id.txt_new:
                txtNew.setTextColor(getResources().getColor(R.color.font_red));
                txtNew.setBackground(getResources().getDrawable(R.drawable.corners_forum_btn_red));
                location = "";
                page = 1;
                getNote();
                break;

            case R.id.txt_top:
                txtTop.setTextColor(getResources().getColor(R.color.font_red));
                txtTop.setBackground(getResources().getDrawable(R.drawable.corners_forum_btn_red));
                location = "A";
                page = 1;
                getNote();
                break;

            case R.id.txt_essence:
                txtEssence.setTextColor(getResources().getColor(R.color.font_red));
                txtEssence.setBackground(getResources().getDrawable(R.drawable.corners_forum_btn_red));
                location = "B";
                page = 1;
                getNote();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(list.size() > 0){
            startActivity(new Intent(getActivity(), NoteActivity.class).putExtra("code",list.get(i).getCode()));
        }
    }

    private void initBtn() {
        txtNew.setTextColor(getResources().getColor(R.color.font_gray_999999));
        txtNew.setBackground(getResources().getDrawable(R.drawable.corners_forum_btn_gray));

        txtTop.setTextColor(getResources().getColor(R.color.font_gray_999999));
        txtTop.setBackground(getResources().getDrawable(R.drawable.corners_forum_btn_gray));

        txtEssence.setTextColor(getResources().getColor(R.color.font_gray_999999));
        txtEssence.setBackground(getResources().getDrawable(R.drawable.corners_forum_btn_gray));
    }

    private void getNote() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId",""));
            object.put("keyword", "");
            object.put("title", "");
            object.put("publisher", "");
            object.put("status", "BD");
            object.put("location", location);
            object.put("isLock", "");
            // 模板编号
            object.put("plateCode", "");
            // 站点编号
            object.put("companyCode", citySp.getString("cityCode",null));
            object.put("dateStart", "");
            object.put("dateEnd", "");
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("orderColumn", "publish_datetime");
            object.put("orderDir", "");
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_610130, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    List<NoteModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<NoteModel>>() {
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
                getNote();
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
                getNote();
            }
        }, 1500);
    }
}
