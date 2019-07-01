package com.zhejiangshegndian.csw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.easeui.EaseConstant;
import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.adapter.NoteAdapter;
import com.zhejiangshegndian.csw.model.AttentionModel;
import com.zhejiangshegndian.csw.model.NoteModel;
import com.zhejiangshegndian.csw.model.PersonalModel;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.ImageTool;
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
import de.hdodenhof.circleimageview.CircleImageView;

import static com.zhejiangshegndian.csw.R.id.txt_attention;

public class PersonActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, AdapterView.OnItemClickListener {

    @InjectView(R.id.list_note)
    ListView listNote;
    @InjectView(R.id.layout_refresh)
    RefreshLayout layoutRefresh;
    @InjectView(R.id.layout_edit)
    LinearLayout layoutEdit;

    // 头部View
    View headerView;

    LinearLayout layoutBack;
    LinearLayout layoutMore;

    ImageView imgGender;
    CircleImageView imgPhoto;

    TextView txtFans;
    TextView txtName;
    TextView txtLevel;
    TextView txtNoteNum;
    TextView txtAttention;
    TextView txtIntroduce;

    private String userId;
    private boolean isSelf = false;
    private PersonalModel model;

    private int page = 1;
    private int pageSize = 10;
    private List<NoteModel> list;
    private NoteAdapter adapter;

    // 关注状态 true: 已关注;false: 未关注;
    private boolean isAttention = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.inject(this);

        inits();
        initHeadView();
        initListView();
        initRefreshLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        getNote();
        getNoteSize();
    }

    private void inits() {
        userId = getIntent().getStringExtra("userId");
        if (userId.equals(userInfoSp.getString("userId", ""))) {
            isSelf = true;
        }

        list = new ArrayList<>();
        model = new PersonalModel();
        adapter = new NoteAdapter(this, list);
    }

    private void initHeadView() {
        headerView = LayoutInflater.from(this).inflate(R.layout.header_person, null);

        layoutBack = (LinearLayout) headerView.findViewById(R.id.layout_back);
        layoutMore = (LinearLayout) headerView.findViewById(R.id.layout_more);

        imgGender = (ImageView) headerView.findViewById(R.id.img_gender);
        imgPhoto = (CircleImageView) headerView.findViewById(R.id.img_photo);

        txtFans = (TextView) headerView.findViewById(R.id.txt_fans);
        txtName = (TextView) headerView.findViewById(R.id.txt_name);
        txtLevel = (TextView) headerView.findViewById(R.id.txt_level);
        txtNoteNum = (TextView) headerView.findViewById(R.id.txt_noteNum);
        txtAttention = (TextView) headerView.findViewById(txt_attention);
        txtIntroduce = (TextView) headerView.findViewById(R.id.txt_introduce);

        if (isSelf) {
            layoutMore.setVisibility(View.GONE);
            layoutEdit.setVisibility(View.VISIBLE);
        }else {
            isAttention();
        }
        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        layoutMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOption(view);
            }
        });

    }

    private void initListView() {
        listNote.setOnItemClickListener(this);
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

    @OnClick(R.id.layout_edit)
    public void onClick() {
        startActivity(new Intent(PersonActivity.this, EditActivity.class));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(list.size() > 0){
            startActivity(new Intent(PersonActivity.this, NoteActivity.class).putExtra("code",list.get(i-1).getCode()));
        }
    }

    /**
     * 获取用户详情
     */
    private void getData() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userId);
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_805256, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<PersonalModel>() {
                    }.getType());

                    setView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(PersonActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(PersonActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView() {
        ImageTool.glide(this, model.getUserExt().getPhoto(), imgPhoto);

        if(model.getUserExt().getGender() != null){
            switch (model.getUserExt().getGender()){
                case "0":
                    imgGender.setImageDrawable(getResources().getDrawable(R.mipmap.female));
                    break;

                case "1":
                    imgGender.setImageDrawable(getResources().getDrawable(R.mipmap.male));
                    break;
            }

        }

        txtName.setText(model.getNickname());
        txtLevel.setText("V" + model.getLevel());

        txtAttention.setText(model.getTotalFollowNum() + "");
        txtFans.setText(model.getTotalFansNum() + "");

        if(model.getUserExt().getIntroduce() == null){
            txtIntroduce.setText("TA有点懒,什么也没留下");
        }else {
            txtIntroduce.setText(model.getUserExt().getIntroduce());
        }

    }

    private void getNote() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", ""));
            object.put("keyword", "");
            object.put("title", "");
            object.put("publisher", userId);
            object.put("status", "BD");
            object.put("location", "");
            object.put("isLock", "");
            // 模板编号
            object.put("plateCode", "");
            // 站点编号
//            object.put("companyCode", citySp.getString("cityCode",null));
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
                Toast.makeText(PersonActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(PersonActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getNoteSize() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", ""));
            object.put("status", "BD");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_610150, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                txtNoteNum.setText(result.split("\\.")[0]+"个帖子");

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(PersonActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(PersonActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void isAttention() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", "");
            object.put("mobile", "");
            object.put("toUser", userId);
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

                    List<String> idList = new ArrayList<String>();
                    for (AttentionModel attentionModel : lists) {
                        idList.add(attentionModel.getUserId());
                    }

                    if (idList.contains(userInfoSp.getString("userId", ""))) {
                        isAttention = true;
                    } else {
                        isAttention = false;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(PersonActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(PersonActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showOption(final View view) {

        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(this).inflate(R.layout.popup_person, null);

        TextView txtAttention = (TextView) mView.findViewById(txt_attention);
        TextView txtCancel = (TextView) mView.findViewById(R.id.txt_cancel);
        TextView txtChat = (TextView) mView.findViewById(R.id.txt_chat);

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

        if (isAttention) {
            txtAttention.setText("取消关注");
        }else {
            txtAttention.setText("+关注");
        }

        txtCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
            }
        });

        txtChat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(PersonActivity.this, ChatActivity.class);
                intent.putExtra("nickName", model.getNickname());
                intent.putExtra("myPhoto", userInfoSp.getString("photo", ""));
                intent.putExtra("myName", userInfoSp.getString("nickName", ""));
                intent.putExtra("otherPhoto", model.getUserExt().getPhoto());
                intent.putExtra(EaseConstant.EXTRA_USER_ID, userId);
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                startActivity(intent);

            }
        });

        txtAttention.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isAttention) {
                    noAttention();
                }else {
                    attention();
                }
                popupWindow.dismiss();
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_share));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    public void attention() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", ""));
            object.put("toUser", userId);
            object.put("token", userInfoSp.getString("token", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post(Constants.CODE_805080, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(PersonActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                isAttention();
                getData();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(PersonActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(PersonActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void noAttention() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", ""));
            object.put("toUser", userId);
            object.put("token", userInfoSp.getString("token", ""));
            object.put("systemCode", appConfigSp.getString("systemCode", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post(Constants.CODE_805081, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(PersonActivity.this, "取消关注成功", Toast.LENGTH_SHORT).show();
                isAttention();
                getData();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(PersonActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(PersonActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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
                getData();
                getNote();
                getNoteSize();
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
