package com.zhejiangshegndian.csw.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.activity.CityActivity;
import com.zhejiangshegndian.csw.activity.ModuleActivity;
import com.zhejiangshegndian.csw.activity.NoteActivity;
import com.zhejiangshegndian.csw.activity.ReleaseActivity;
import com.zhejiangshegndian.csw.activity.SearchActivity;
import com.zhejiangshegndian.csw.activity.StoreActivity;
import com.zhejiangshegndian.csw.activity.WebActivity;
import com.zhejiangshegndian.csw.adapter.NewsAdapter;
import com.zhejiangshegndian.csw.adapter.PagerAdapter;
import com.zhejiangshegndian.csw.listener.BannerPageChangeListener;
import com.zhejiangshegndian.csw.loader.BannerImageLoader;
import com.zhejiangshegndian.csw.model.BannerModel;
import com.zhejiangshegndian.csw.model.ChildSystemModel;
import com.zhejiangshegndian.csw.model.CityModel;
import com.zhejiangshegndian.csw.model.GridModel;
import com.zhejiangshegndian.csw.model.NoteModel;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.MapTool;
import com.zhejiangshegndian.csw.tool.SignInTool;
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

import static com.zhejiangshegndian.csw.tool.Constants.ACTIVE;

/**
 * Created by LeiQ on 2017//3.
 */

public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {

    @InjectView(R.id.layout_search)
    LinearLayout layoutSearch;
    @InjectView(R.id.txt_city)
    TextView txtCity;
    @InjectView(R.id.layout_release)
    LinearLayout layoutRelease;
    @InjectView(R.id.list_news)
    ListView listNews;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;
    @InjectView(R.id.layout_city)
    LinearLayout layoutCity;

    // 站点名称
    private String cityName;
    // 站点编号
    private String cityCode;

    // View
    private View view;

    private View headerView;
    private Banner banner;
    private ViewPager viewPager;
    private FrameLayout signLyaout;
    private FrameLayout storeLyaout;
    private FrameLayout activeLyaout;
    private TextView txtSign;
    private TextView txtStore;
    private TextView txtActive;
    private ImageView imgSign;
    private ImageView imgStore;
    private ImageView imgActive;

    // 子系统URL
    private String signUrl = "";
    private String storeUrl = "";
    private String activeUrl = "";

    // 轮播图数据
    private List<String> images;
    private List<String> titles;
    private List<BannerModel> listBanner;

    // 模块
    private List<ChildSystemModel> listChildSystem;
    // 传递的数据
    private android.support.v4.view.PagerAdapter pageAdapter;
    private ArrayList<GridModel> modelList;
    private ArrayList<Fragment> fragments;

    private int page = 1;
    private int pageSize = 10;
    private List<NoteModel> list;
    private NewsAdapter newsAdapter;

    private SharedPreferences citySp;
    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, null);
        ButterKnife.inject(this, view);

        inits();
        initHeaderView(inflater);
        initRefreshLayout();

        initCity();
        initListView();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void inits() {
        list = new ArrayList<>();
        images = new ArrayList<>();
        titles = new ArrayList<>();
        listBanner = new ArrayList<>();

        modelList = new ArrayList<>();
        fragments = new ArrayList<>();
        listChildSystem = new ArrayList<>();

        newsAdapter = new NewsAdapter(getActivity(), list);

        citySp = getActivity().getSharedPreferences("city", Context.MODE_PRIVATE);
        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getActivity().getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    private void initHeaderView(LayoutInflater inflater) {
        headerView = inflater.inflate(R.layout.header_news, null);

        banner = (Banner) headerView.findViewById(R.id.banner);

        txtSign = (TextView) headerView.findViewById(R.id.txt_sign);
        txtStore = (TextView) headerView.findViewById(R.id.txt_shop);
        txtActive = (TextView) headerView.findViewById(R.id.txt_active);

        imgSign = (ImageView) headerView.findViewById(R.id.img_sign);
        imgStore = (ImageView) headerView.findViewById(R.id.img_shop);
        imgActive = (ImageView) headerView.findViewById(R.id.img_active);

        signLyaout = (FrameLayout) headerView.findViewById(R.id.layout_sign);
        storeLyaout = (FrameLayout) headerView.findViewById(R.id.layout_shop);
        activeLyaout = (FrameLayout) headerView.findViewById(R.id.layout_active);

        viewPager = (ViewPager) headerView.findViewById(R.id.viewpager_layout);

        storeLyaout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initUrl(storeUrl);
            }
        });

        signLyaout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initUrl(signUrl);
            }
        });

        activeLyaout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SignInTool.isSignIn(getActivity())) {
                    initUrl(activeUrl);
                }

            }
        });
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setOnLoadListener(this);
    }

    private void initCity() {
        MapTool.getLocation(getActivity(), new MapTool.locationCallBack() {
            @Override
            public void onSuccess(String latitude, String longitude, String location, String province, String city, String district) {

                // 根据省市区查询站点
                getCity(province, city, district);
            }

            @Override
            public void onFailed(String tip) {
//                Toast.makeText(SignUpActivity.this, tip + ",请手动选择站点", Toast.LENGTH_SHORT).show();
                txtCity.setText(tip + ",请手动选择站点");
            }
        });
    }

    private void initListView() {
        listNews.addHeaderView(headerView);
        listNews.setAdapter(newsAdapter);

        listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), NoteActivity.class).putExtra("code",list.get(i-1).getCode()));
            }
        });
    }

    private void initBanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        banner.setImageLoader(new BannerImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置banner点击事件
        banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                initUrl(listBanner.get(position-1).getUrl());
            }
        });
        // 设置在操作Banner时listView事件不触发
        banner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    listNews.requestDisallowInterceptTouchEvent(false);
                } else {
                    listNews.requestDisallowInterceptTouchEvent(true);
                }
                return true;
            }
        });
        // 设置在操作Banner时listView事件不触发
        banner.setOnPageChangeListener(new BannerPageChangeListener(swipeContainer));
        //banner设置方法全部调用完毕时最后调用
        banner.start();

    }

    @OnClick({R.id.layout_search, R.id.layout_city, R.id.layout_release})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;

            case R.id.layout_city:
                startActivityForResult(new Intent(getActivity(), CityActivity.class),0);
                break;

            case R.id.layout_release:
                if(SignInTool.isSignIn(getActivity())){
                    startActivity(new Intent(getActivity(), ReleaseActivity.class));
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){
            cityName = data.getStringExtra("cityName");
            cityCode = data.getStringExtra("cityCode");

            setCity();

            page = 1;
            getNote();
            getBanner();
            getChildSystem();
        }
    }

    private void getCity(String province, String city, String district) {
        JSONObject object = new JSONObject();
        try {
            object.put("status", "2");
            object.put("province", province.substring(0, province.length()-1));
            object.put("city", city.substring(0, province.length()-1));
            object.put("area", district);
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_806012, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Gson gson = new Gson();
                    CityModel model = gson.fromJson(jsonObject.toString(),new TypeToken<CityModel>(){}.getType());

                    if(model != null){
                        cityName = model.getName();
                        cityCode = model.getCode();
                    }else{
                        cityName = "";
                        cityCode = "";
                    }

                    setCity();
                    getNote();
                    getBanner();
                    getChildSystem();

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

    private void setCity() {
        txtCity.setText(cityName);

        SharedPreferences.Editor editor = citySp.edit();
        editor.putString("cityName",cityName);
        editor.putString("cityCode",cityCode);
        editor.commit();

    }

    private void getBanner() {
        JSONObject object = new JSONObject();
        try {
            object.put("companyCode", cityCode);
            object.put("location", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_610107, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    Gson gson = new Gson();
                    List<BannerModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<BannerModel>>() {
                    }.getType());

                    images.clear();
                    titles.clear();
                    listBanner.clear();
                    listBanner.addAll(lists);
                    for (BannerModel model : lists) {
                        if(model.getPic() != null && model.getName() != null){
                            images.add(model.getPic());
                            titles.add(model.getName());
                        }
                    }
                    initBanner();

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

    private void getNote() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", "");
            object.put("keyword", "");
            object.put("title", "");
            object.put("publisher", "");
            object.put("status", "BD");
            object.put("location", "C");
            object.put("isLock", "");
            // 模板编号
            object.put("plateCode", "");
            // 站点编号
            object.put("companyCode", cityCode);
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

                    if(page == 1){
                        list.clear();
                    }
                    list.addAll(lists);
                    newsAdapter.notifyDataSetChanged();

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

    private void getChildSystem() {
        JSONObject object = new JSONObject();
        try {
            object.put("companyCode", citySp.getString("cityCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_610097, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    Gson gson = new Gson();
                    List<ChildSystemModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<ChildSystemModel>>() {
                    }.getType());

                    if(page == 1){
                        list.clear();
                    }
                    listChildSystem.addAll(lists);



                    modelList = new ArrayList<GridModel>();
                    for (int i = 0; i < listChildSystem.size(); i++) {
                        if(listChildSystem.get(i).getLocation() == 1){
                            GridModel model = new GridModel(i + "", i + "name");
                            model.setName(listChildSystem.get(i).getName());
                            model.setImage(listChildSystem.get(i).getPic());
                            model.setUrl(listChildSystem.get(i).getUrl());
                            modelList.add(model);
                        }else {
                            switch (listChildSystem.get(i).getOrderNo()){
                                case 1:
                                    storeUrl = listChildSystem.get(i).getUrl();
                                    txtStore.setText(listChildSystem.get(i).getName());
//                                    ImageTool.glide(getActivity(),listChildSystem.get(i).getPic(),imgStore);
                                    break;

                                case 2:
                                    signUrl = listChildSystem.get(i).getUrl();
                                    txtSign.setText(listChildSystem.get(i).getName());
//                                    ImageTool.glide(getActivity(),listChildSystem.get(i).getPic(),imgSign);
                                    break;

                                case 3:
                                    activeUrl = listChildSystem.get(i).getUrl();
                                    txtActive.setText(listChildSystem.get(i).getName());
//                                    ImageTool.glide(getActivity(),listChildSystem.get(i).getPic(),imgActive);
                                    break;
                            }
                        }
                    }

                    int size = modelList.size();
                    fragments.clear();

                    int pageCount;
                    if (size % 8 == 0) {
                        pageCount = size / 8;
                    } else {
                        pageCount = size / 8 + 1;
                    }

                    for (int i = 0; i < pageCount; i++) {
                        //初始化每一个fragment
                        ModuleFragment gf = ModuleFragment.newInstance(i, modelList);
                        fragments.add(gf);
                    }

                    //初始化pageAdapter
                    pageAdapter = new PagerAdapter(getActivity().getSupportFragmentManager(),
                            fragments);

                    viewPager.setAdapter(pageAdapter);

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

    private void sign() {
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token",""));
            object.put("userId", userInfoSp.getString("userId",""));
            object.put("systemCode", appConfigSp.getString("systemCode",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_805100, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    showSign(true,jsonObject.getDouble("amount"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void onTip(String tip) {
                showSign(false,0);
//                Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(getActivity(), "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSign(boolean isSucceed,double amount) {

        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_sign, null);

        ImageView imgSign = (ImageView) mView.findViewById(R.id.img_sign);
        TextView txtAmount = (TextView) mView.findViewById(R.id.txt_amount);
        LinearLayout layoutAmount = (LinearLayout) mView.findViewById(R.id.layout_amount);
        LinearLayout layoutSign = (LinearLayout) mView.findViewById(R.id.layout_sign);

        final PopupWindow popupWindow = new PopupWindow(mView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setTouchable(true);
//        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });


        if(isSucceed){
            imgSign.setImageResource(R.mipmap.sign_succeed);
            txtAmount.setText((amount/1000)+"");
            layoutAmount.setVisibility(View.VISIBLE);
        }else {
            imgSign.setImageResource(R.mipmap.sign_failure);
        }

        layoutSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_share));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    /**
     * 初始化子系统URL
     */
    public void initUrl(String url){
        if(url.indexOf("http") != -1){
            // url 包含"http"
            startActivity(new Intent(getActivity(), WebActivity.class).putExtra("url",url));
        }else{
            String str[] = url.split(",");
            if(str[0].split("\\:")[0].equals("page")){
                switch (str[0].split("\\:")[1]){
                    case "mall":
                        startActivity(new Intent(getActivity(), StoreActivity.class));
                        break;

                    case "signin":
                        sign();
                        break;

                    case "activity":
                        System.out.println("-------> "+ACTIVE+cityCode+"&tk="+userInfoSp.getString("token",""));
                        startActivity(new Intent(getActivity(), WebActivity.class)
                                .putExtra("url",ACTIVE+cityCode+"&tk="+userInfoSp.getString("token","")));
                        break;

                    case "board":
                        startActivity(new Intent(getActivity(), ModuleActivity.class)
                                .putExtra("plateCode",  str[1].split("\\:")[1]));
                        break;
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
                page = 1;
                getNote();
                getBanner();
                getChildSystem();

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
                getNote();
            }
        }, 1500);
    }

}
