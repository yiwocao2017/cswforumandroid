package com.zhejiangshegndian.csw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.adapter.YouLiaoAdapter;
import com.zhejiangshegndian.csw.model.NoteModel;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.ImageTool;
import com.zhejiangshegndian.csw.tool.SignInTool;
import com.zhejiangshegndian.csw.tool.Xutil;
import com.zhejiangshegndian.csw.view.RefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ModuleActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, AdapterView.OnItemClickListener  {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_title)
    TextView txtTitle;
    @InjectView(R.id.layout_city)
    LinearLayout layoutCity;
    @InjectView(R.id.layout_release)
    LinearLayout layoutRelease;
    @InjectView(R.id.layout_search)
    LinearLayout layoutSearch;
    @InjectView(R.id.list_note)
    ListView listNote;
    @InjectView(R.id.layout_refresh)
    RefreshLayout layoutRefresh;

    // 模板编号
    String plateCode;
    String plateName;

    View headerView;

    ImageView imgModule;
    TextView txtName;
    TextView txtTheme;
    TextView txtToday;
    TextView txtAll;
    TextView txtNew;
    TextView txtEssence;
    ImageView lineAll;
    ImageView lineNew;
    ImageView lineEssence;

    String location = "";

    int page = 1;
    int pageSize = 10;
    List<NoteModel> list;
    YouLiaoAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
        ButterKnife.inject(this);

        inis();
        initHeaderView();
        initListView();
        initRefreshLayout();

    }

    @Override
    protected void onResume() {
        super.onResume();

        getNote();
        getModule();
    }

    private void inis() {
        plateCode = getIntent().getStringExtra("plateCode");

        list = new ArrayList<>();
        adapter = new YouLiaoAdapter(this, list);
    }

    private void initHeaderView() {
        headerView = LayoutInflater.from(this).inflate(R.layout.header_module, null);

        imgModule = (ImageView) headerView.findViewById(R.id.img_module);
        txtName = (TextView) headerView.findViewById(R.id.txt_name);
        txtTheme = (TextView) headerView.findViewById(R.id.txt_theme);
        txtToday = (TextView) headerView.findViewById(R.id.txt_today);
        txtAll = (TextView) headerView.findViewById(R.id.txt_all);
        txtNew = (TextView) headerView.findViewById(R.id.txt_new);
        txtEssence = (TextView) headerView.findViewById(R.id.txt_essence);

        lineAll = (ImageView) headerView.findViewById(R.id.line_all);
        lineNew = (ImageView) headerView.findViewById(R.id.line_new);
        lineEssence = (ImageView) headerView.findViewById(R.id.line_essence);

        txtAll.setOnClickListener(new HeaderOnClickListener());
        txtNew.setOnClickListener(new HeaderOnClickListener());
        txtEssence.setOnClickListener(new HeaderOnClickListener());
    }

    private void initListView() {
        listNote.addHeaderView(headerView);
        listNote.setAdapter(adapter);
    }

    private void initRefreshLayout() {
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        layoutRefresh.setOnRefreshListener(this);
        layoutRefresh.setOnLoadListener(this);

    }

    @OnClick({R.id.layout_back, R.id.layout_release, R.id.layout_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_release:
                if(SignInTool.isSignIn(ModuleActivity.this)){
                    startActivity(new Intent(ModuleActivity.this, ReleaseActivity.class)
                            .putExtra("moduleName",plateName)
                            .putExtra("moduleCode",plateCode));
                }
                break;

            case R.id.layout_search:
                startActivity(new Intent(ModuleActivity.this, SearchActivity.class));
                break;
        }
    }

    public class HeaderOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            initLine();
            switch (view.getId()){
                case R.id.txt_all:
                    lineAll.setBackgroundColor(getResources().getColor(R.color.font_red));
                    location = "";
                    getNote();
                    break;

                case R.id.txt_new:
                    lineNew.setBackgroundColor(getResources().getColor(R.color.font_red));
                    location = "";
                    getNote();
                    break;

                case R.id.txt_essence:
                    lineEssence.setBackgroundColor(getResources().getColor(R.color.font_red));
                    location = "B";
                    getNote();
                    break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(list.size() > 0){
            startActivity(new Intent(ModuleActivity.this, NoteActivity.class).putExtra("code",list.get(i).getCode()));
        }
    }

    private void initLine(){
        lineAll.setBackgroundColor(getResources().getColor(R.color.lineColor));
        lineNew.setBackgroundColor(getResources().getColor(R.color.lineColor));
        lineEssence.setBackgroundColor(getResources().getColor(R.color.lineColor));
    }

    private void getNote() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", ""));
            object.put("keyword", "");
            object.put("title", "");
            object.put("publisher", "");
            object.put("status", "BD");
            object.put("location", location);
            object.put("isLock", "");
            // 模板编号
            object.put("plateCode", plateCode);
            // 站点编号
            object.put("companyCode", "");
            object.put("dateStart", "");
            object.put("dateEnd", "");
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("orderColumn", "");
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
                Toast.makeText(ModuleActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ModuleActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getModule() {
        JSONObject object = new JSONObject();
        try {
            object.put("code", plateCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_610046, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    ImageTool.glide(ModuleActivity.this, jsonObject.getJSONObject("splate").getString("pic"), imgModule);
                    plateName = jsonObject.getJSONObject("splate").getString("name");
                    txtName.setText(plateName);
                    txtTheme.setText(jsonObject.getString("allPostCount"));
                    txtToday.setText(jsonObject.getString("todayPostCount"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ModuleActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ModuleActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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
