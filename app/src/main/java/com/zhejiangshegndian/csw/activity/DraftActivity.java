package com.zhejiangshegndian.csw.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.adapter.DraftAdapter;
import com.zhejiangshegndian.csw.model.NoteModel;
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

public class DraftActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, AdapterView.OnItemClickListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_draft)
    ListView listDraft;
    @InjectView(R.id.layout_refresh)
    RefreshLayout layoutRefresh;

    private int page = 1;
    private int pageSize = 10;
    private List<NoteModel> list;
    private DraftAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft);
        ButterKnife.inject(this);
        inits();
        initListView();
        initRefreshLayout();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getNote();
    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new DraftAdapter(this, list);
    }

    private void initListView() {
        listDraft.setOnItemClickListener(this);
        listDraft.setAdapter(adapter);
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
        if(list.size() > 0){
            showDelete(view,list.get(i).getCode());
        }
    }

    private void showDelete(final View view, final String draftCode) {

        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(this).inflate(R.layout.popup_note_my_comment, null);

        TextView txtDelete = (TextView) mView.findViewById(R.id.txt_delete);
        TextView txtCancel = (TextView) mView.findViewById(R.id.txt_cancel);

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

        txtDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
                // 删除草稿
                delete("1",draftCode);
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
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

    private void getNote() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId",""));
            object.put("keyword", "");
            object.put("title", "");
            object.put("publisher", "");
            object.put("status", "A");
            object.put("location", "");
            object.put("isLock", "");
            // 模板编号
            object.put("plateCode", "");
            // 站点编号
            object.put("companyCode", citySp.getString("cityCode",null));
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
                Toast.makeText(DraftActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(DraftActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void delete(final String type, String code) {
        JSONArray codeList = new JSONArray();
        codeList.put(code);

        JSONObject object = new JSONObject();
        try {
            object.put("codeList", codeList);
            object.put("userId", userInfoSp.getString("userId", ""));
            // 1:帖子,2:评论
            object.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post(Constants.CODE_610116, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(DraftActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                page = 1;
                getNote();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(DraftActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(DraftActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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