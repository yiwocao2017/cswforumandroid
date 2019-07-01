package com.zhejiangshegndian.csw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.adapter.CityAdapter;
import com.zhejiangshegndian.csw.model.CityModel;
import com.zhejiangshegndian.csw.tool.CharacterParser;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.DeviceTool;
import com.zhejiangshegndian.csw.tool.Xutil;
import com.zhejiangshegndian.csw.view.SideBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author leiQ
 * @version 选择站点（城市）
 */
public class CityActivity extends MyBaseActivity {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.edt_search)
    EditText edtSearch;
    @InjectView(R.id.txt_cancel)
    TextView txtCancel;
    @InjectView(R.id.list_city)
    ListView listCity;
    @InjectView(R.id.txt_dialog)
    TextView txtDialog;
    @InjectView(R.id.sideBar)
    SideBar sideBar;

    private TextView txtNone;
    private TextView txtLocation;
    private LinearLayout layoutHots;
    private LinearLayout layoutRecommended;

    private String[] city = {""};

    private View headerView;

    private CityAdapter adapter;
    // 热门站点
    private List<CityModel> hots;
    // 全部站点
    private List<CityModel> citys;
    private List<CityModel> datas;

    private String cityName;
    private String cityCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.inject(this);

        inits();
        initEditText();
        initHeaderView();
        initListView();
        initSideBar();

        getCity("");
    }

    private void inits() {
        hots = new ArrayList<>();
        citys = new ArrayList<>();
        datas = new ArrayList<>();
        adapter = new CityAdapter(this, datas);

        cityName = citySp.getString("cityName","");
        cityCode = citySp.getString("cityCode","");
    }

    private void initEditText() {
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    Log.i("SEARCH_INPUT",edtSearch.getText().toString());
                    DeviceTool.hideKeyword(CityActivity.this);
                    getCity(edtSearch.getText().toString());
                }
                return false;
            }
        });
    }

    private void initHeaderView() {
        headerView = LayoutInflater.from(this).inflate(R.layout.header_city,null);

        txtNone = (TextView) headerView.findViewById(R.id.txt_none);
        txtLocation = (TextView) headerView.findViewById(R.id.txt_location);

        layoutHots = (LinearLayout) headerView.findViewById(R.id.layout_hots);
        layoutRecommended = (LinearLayout) headerView.findViewById(R.id.layout_recommended);

        // 当前站点
        txtLocation.setText(cityName);
        txtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(0,new Intent().putExtra("cityName",cityName).putExtra("cityCode",cityCode));
                finish();
            }
        });
    }

    private void initListView(){
        // 排序
        Collections.sort(datas, new Comparator<CityModel>() {

            // -1代表o1里的某一个属性比o2的小 0代表等于 1代表大于
            @Override
            public int compare(CityModel o1, CityModel o2) {
                if (o1.getSortLetters().equals("#"))
                    return 1;
                else if (o2.getSortLetters().equals("#"))
                    return -1;
                else
                    return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        });
        listCity.addHeaderView(headerView);
        listCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(i != 0){
                    cityName = citys.get(i-1).getName();
                    cityCode = citys.get(i-1).getCode();
                    setResult(0,new Intent().putExtra("cityName",cityName).putExtra("cityCode",cityCode));
                    finish();
                }
            }
        });
        listCity.setAdapter(adapter);
    }

    private void initSideBar(){
        sideBar.setTextView(txtDialog);

        sideBar.setOnSelectChangeListener(new SideBar.OnSelectChangeListener() {

            @Override
            public void onSelectChange(String letter) {
                int ascii = letter.charAt(0);
                int position = adapter.getPositionFroAscii(ascii);
                if (position != -1)
                    listCity.setSelection(position);
            }
        });
    }

    private List<CityModel> getDatas(String[] array) {
        List<CityModel> datas = new ArrayList<CityModel>();
        for (int i = 0; i < array.length; i++) {
            CityModel sortModel = new CityModel();
            sortModel.setName(array[i]);
            String abc = CharacterParser.getInstance().getSelling(array[i]);
            String sortString = abc.substring(0, 1).toUpperCase();
            // 正则表达式
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString);
            } else {
                sortModel.setSortLetters("#");
            }
            datas.add(sortModel);
        }
        return datas;
    }

    private void getCity(String condition) {


        JSONObject object = new JSONObject();
        try {
            object.put("code", "");
            object.put("name", condition);
            object.put("abbrName", "");
            object.put("userId", "");
            object.put("location", "");
            object.put("status", "2");
            object.put("isDefault", "");
            object.put("isHot", "");
            object.put("province", "");
            object.put("city", "");
            object.put("area", "");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_806017, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

//                    Gson gson = new Gson();
//                    CitysModel model = gson.fromJson(jsonObject.toString(), new TypeToken<CitysModel>() {}.getType());
//
//                    citys.clear();
//                    citys.addAll(CityUtil.getCity(model));
//                    if(citys.size() != 0){
//                        setData();
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(CityActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(CityActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {
        // 分离热门数据,isHot = 1为热门
        hots.clear();
        for (CityModel model : citys){
            if(model.getIsHot().equals("1")){
                hots.add(model);
            }
        }

        // 组装站点名称
        city = new String[citys.size()];
        for (int i = 0; i < citys.size(); i++){
            city[i] = citys.get(i).getName();

        }

        List<CityModel> l = getDatas(city);
        datas.clear();
        datas.addAll(l);
        adapter.notifyDataSetChanged();

        setHots();
    }

    private void setHots() {
        if(hots.size() == 0){
            layoutRecommended.setVisibility(View.GONE);
//            txtNone.setVisibility(View.VISIBLE);
        }else{
            for (int i = 0; i < hots.size(); i++){
                final int index  = i;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,DeviceTool.dip2px(this,35f));
                layoutParams.setMargins(DeviceTool.dip2px(this,15f),0,0,0);
                view.setLayoutParams(layoutParams);
                view.setText(hots.get(i).getName());
                view.setGravity(Gravity.CENTER_VERTICAL);
                view.setTextColor(getResources().getColor(R.color.font_gray));

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cityName = hots.get(index).getName();
                        cityCode = hots.get(index).getCode();

                        System.out.println("cityName="+cityName);
                        System.out.println("cityCode="+cityCode);

                        setResult(0,new Intent().putExtra("cityName",cityName).putExtra("cityCode",cityCode));
                        finish();
                    }
                });

                layoutHots.addView(view);
            }
        }
    }

    @OnClick({R.id.layout_back, R.id.txt_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_cancel:
                setResult(-1);
                finish();
                break;
        }
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
