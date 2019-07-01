package com.zhejiangshegndian.csw.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.activity.ModuleActivity;
import com.zhejiangshegndian.csw.adapter.ForumAdapter;
import com.zhejiangshegndian.csw.adapter.ForumModuleAdapter;
import com.zhejiangshegndian.csw.model.ForumModel;
import com.zhejiangshegndian.csw.model.ModuleModel;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 论坛Fragment
 * Created by LeiQ on 2017/3/8.
 */

public class ForumLTFragment extends Fragment {

    @InjectView(R.id.list_forum)
    ListView listForum;
    @InjectView(R.id.list_module)
    ListView listModule;
    private View view;

    private List<ForumModel> forumList;
    private ForumAdapter forumAdapter;
    private List<ModuleModel> moduleList;
    private ForumModuleAdapter moduleAdapter;

    public SharedPreferences citySp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_luntan, null);
        ButterKnife.inject(this, view);

        inits();
        initListView();

        getForum();

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            getForum();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getForum();
    }

    private void inits() {
        forumList = new ArrayList<>();
        moduleList = new ArrayList<>();

        forumAdapter = new ForumAdapter(getActivity(),forumList);
        moduleAdapter = new ForumModuleAdapter(getActivity(),moduleList);

        citySp = getActivity().getSharedPreferences("city", Context.MODE_PRIVATE);
    }

    private void initListView() {
        listForum.setAdapter(forumAdapter);
        listForum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (ForumModel model: forumList){
                    model.setSelect(false);
                }
                forumList.get(i).setSelect(true);
                forumAdapter.notifyDataSetChanged();
                getModule(forumList.get(i).getCode());
            }
        });

        listModule.setAdapter(moduleAdapter);
        listModule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), ModuleActivity.class).putExtra("plateCode",moduleList.get(i).getCode()));
            }
        });

    }

    public void getForum(){
        JSONObject object = new JSONObject();
        try {
            object.put("parentCode", "");
            object.put("userId", "");
            object.put("companyCode", citySp.getString("cityCode", null));
            object.put("orderColumn", "order_no");
            object.put("orderDir", "asc");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_610027, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    Gson gson = new Gson();
                    List<ForumModel> lists = gson.fromJson(jsonArray.toString(),new TypeToken<List<ForumModel>>(){}.getType());

                    forumList.clear();
                    forumList.addAll(lists);
                    if(forumList.size() > 0){
                        forumList.get(0).setSelect(true);
                        getModule(forumList.get(0).getCode());
                    }
                    forumAdapter.notifyDataSetChanged();

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

    public void getModule(String parentCode){
        JSONObject object = new JSONObject();
        try {
            object.put("parentCode", parentCode);
            object.put("userId", "");
            object.put("companyCode", citySp.getString("cityCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_610047, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    Gson gson = new Gson();
                    List<ModuleModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<ModuleModel>>() {
                    }.getType());

                    moduleList.clear();
                    moduleList.addAll(lists);
                    moduleAdapter.notifyDataSetChanged();

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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
