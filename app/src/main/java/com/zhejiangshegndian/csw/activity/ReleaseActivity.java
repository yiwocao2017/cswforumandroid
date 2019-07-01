package com.zhejiangshegndian.csw.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiniu.android.http.ResponseInfo;
import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.adapter.PagerAdapter;
import com.zhejiangshegndian.csw.adapter.RecyclerViewAdapter;
import com.zhejiangshegndian.csw.adapter.ReleaseAdapter;
import com.zhejiangshegndian.csw.fragment.EmojiFragment;
import com.zhejiangshegndian.csw.model.EmotionsModel;
import com.zhejiangshegndian.csw.model.ModuleModel;
import com.zhejiangshegndian.csw.model.NoteModel;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.QiNiuUtil;
import com.zhejiangshegndian.csw.tool.SaxTool;
import com.zhejiangshegndian.csw.tool.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhejiangshegndian.csw.tool.ImageTool.RESULT_CAMARA_IMAGE;
import static com.zhejiangshegndian.csw.tool.ImageTool.RESULT_LOAD_IMAGE;
import static com.zhejiangshegndian.csw.tool.ImageTool.album;
import static com.zhejiangshegndian.csw.tool.ImageTool.camara;

public class ReleaseActivity extends MyBaseActivity {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.layout_module)
    LinearLayout layoutModule;
    @InjectView(R.id.txt_release)
    TextView txtRelease;
    @InjectView(R.id.edt_title)
    EditText edtTitle;
    @InjectView(R.id.edt_content)
    public EditText edtContent;
    @InjectView(R.id.txt_textNum)
    TextView txtTextNum;
    @InjectView(R.id.layout_image)
    LinearLayout layoutImage;
    @InjectView(R.id.layout_call)
    LinearLayout layoutCall;
    @InjectView(R.id.layout_emoji)
    LinearLayout layoutEmoji;
    @InjectView(R.id.img_add)
    ImageView imgAdd;
    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;
    @InjectView(R.id.txt_title)
    TextView txtTitle;
    @InjectView(R.id.layout_view)
    LinearLayout layoutView;
    @InjectView(R.id.grid_module)
    GridView gridModule;
    @InjectView(R.id.layout_grid)
    LinearLayout layoutGrid;
    @InjectView(R.id.layout_hide)
    LinearLayout layoutHide;
    @InjectView(R.id.layout_delete)
    LinearLayout layoutDelete;
    @InjectView(R.id.viewpager_emojis)
    ViewPager viewpagerEmojis;
    @InjectView(R.id.txt_indicator1)
    TextView txtIndicator1;
    @InjectView(R.id.txt_indicator2)
    TextView txtIndicator2;
    @InjectView(R.id.txt_indicator3)
    TextView txtIndicator3;
    @InjectView(R.id.txt_indicator4)
    TextView txtIndicator4;
    @InjectView(R.id.txt_indicator5)
    TextView txtIndicator5;
    @InjectView(R.id.txt_indicator6)
    TextView txtIndicator6;
    @InjectView(R.id.layout_indicator)
    LinearLayout layoutIndicator;
    @InjectView(R.id.layout_emojis)
    RelativeLayout layoutEmojis;

    private View footView;

    // xml
    private SaxTool saxTool;
    private List<EmotionsModel> emotions;

    // 草稿编号
    private String draftCode = "";
    private NoteModel model;

    // 模板
    private String moduleCode = "";
    private String moduleName = "";
    private List<ModuleModel> moduleList;
    private ReleaseAdapter moduleAdapter;

    private List<String> listPic;
    private List<String> listPicUrl;
    private RecyclerViewAdapter recyclerViewAdapter;

    // 表情
    private ArrayList<Fragment> fragments;
    private PagerAdapter pageAdapter;
    private int emojiPageCount;
    private List<TextView> listIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        ButterKnife.inject(this);

        inits();
        initGridView();
        initEditText();
        initViewPage();
        initRecyclerView();
        getModule();
    }

    public void inits() {
        listPic = new ArrayList<>();
        emotions = new ArrayList<>();
        fragments = new ArrayList<>();
        listPicUrl = new ArrayList<>();
        moduleList = new ArrayList<>();
        listIndicator = new ArrayList<>();

        moduleAdapter = new ReleaseAdapter(ReleaseActivity.this, moduleList);
        pageAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);

        moduleCode = getIntent().getStringExtra("moduleCode");
        moduleName = getIntent().getStringExtra("moduleName");

        draftCode = getIntent().getStringExtra("code");
        if (draftCode != null) {
            getNote();
        }
    }

    private void initGridView() {
//        gridModule.addFooterView(footView);
        gridModule.setAdapter(moduleAdapter);
        gridModule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                txtTitle.setText(moduleList.get(i).getName());
                moduleCode = moduleList.get(i).getCode();
                layoutGrid.setVisibility(View.GONE);
            }
        });
    }

    private void initEditText() {
        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtTextNum.setText(editable.toString().length() + "");
            }
        });
    }

    private void initViewPage() {
        listIndicator.add(txtIndicator1);
        listIndicator.add(txtIndicator2);
        listIndicator.add(txtIndicator3);
        listIndicator.add(txtIndicator4);
        listIndicator.add(txtIndicator5);
        listIndicator.add(txtIndicator6);

        viewpagerEmojis.setAdapter(pageAdapter);
        viewpagerEmojis.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                for (int i = 0; i < emojiPageCount; i++) {
                    if(i == arg0){
                        listIndicator.get(i).setBackground(getResources().getDrawable(R.drawable.corners_indicator_orange));
                    }else{
                        listIndicator.get(i).setBackground(getResources().getDrawable(R.drawable.corners_indicator_gray));
                    }
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void initRecyclerView() {
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReleaseActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        recyclerViewAdapter = new RecyclerViewAdapter(ReleaseActivity.this, listPic, listPicUrl);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @OnClick({R.id.layout_back, R.id.layout_module, R.id.txt_release, R.id.img_add,
            R.id.layout_call, R.id.layout_emoji, R.id.layout_hide, R.id.layout_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                if (draftCheck()) {
                    tip();
                } else {
                    finish();
                }
                break;

            case R.id.layout_module:
                layoutGrid.setVisibility(View.VISIBLE);
                break;

            case R.id.txt_release:
                if (releaseCheck()) {
                    if (draftCode != null) {
                        draftToNote();
                    } else {
                        release("1");
                    }
                }
                break;

            case R.id.img_add:
                choosePhoto(view);
                break;

            case R.id.layout_call:
                startActivityForResult(new Intent(ReleaseActivity.this, LinkManActivity.class), 0);
                break;

            case R.id.layout_emoji:
                if(layoutEmojis.getVisibility() == View.VISIBLE){
                    layoutEmojis.setVisibility(View.GONE);

                }else{
                    try {
                        emotions = SaxTool.sax2xml(getResources().getAssets().open("emoji.xml"));
                        if (emotions.size() > 0) {
                            hideSoftInput();
                            layoutEmojis.setVisibility(View.VISIBLE);
                            initEmoji();
                        } else {
                            Toast.makeText(this, "暂无表情", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;

            case R.id.layout_hide:
                layoutGrid.setVisibility(View.GONE);
                break;

            case R.id.layout_delete:
                edtTitle.setText("");
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                if (data.getData() != null) {
                    System.out.println("album(ReleaseActivity.this,data)=" + album(ReleaseActivity.this, data));
                    listPic.add(album(ReleaseActivity.this, data));
                    recyclerViewAdapter.notifyDataSetChanged();

                    new QiNiuUtil(ReleaseActivity.this, album(ReleaseActivity.this, data), null).qiNiu(new QiNiuUtil.QiNiuCallBack() {
                        @Override
                        public void onSuccess(String key, ResponseInfo info, JSONObject res) {
                            System.out.println("key=" + key);

                            listPicUrl.add(key);
                        }
                    }, true);
                }

            } else if (requestCode == RESULT_CAMARA_IMAGE) {
                if (data.getExtras() != null) {
                    listPic.add(camara(ReleaseActivity.this, data));
                    recyclerViewAdapter.notifyDataSetChanged();

                    new QiNiuUtil(ReleaseActivity.this, camara(ReleaseActivity.this, data), null).qiNiu(new QiNiuUtil.QiNiuCallBack() {
                        @Override
                        public void onSuccess(String key, ResponseInfo info, JSONObject res) {
                            listPicUrl.add(key);
                        }
                    }, true);
                }
            } else if (resultCode == 0) {
                String nickName = data.getStringExtra("nickName");
                String call = "@" + nickName + " ";

//                edtComment.setText(Html.fromHtml(edtComment.getText().toString().trim() + "<font color='#d23e3e'>"+call+"</font>"));
                edtContent.setText(edtContent.getText().toString().trim() + call);
                Editable etext = edtContent.getText();
                Selection.setSelection(etext, etext.length());
            }
        }

    }

    public void getModule() {
        JSONObject object = new JSONObject();
        try {
            object.put("parentCode", "");
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

                    if (moduleName == null && moduleCode == null) {
                        for (ModuleModel model : moduleList) {
                            if (model.getIsDefault().equals("1")) {
                                txtTitle.setText(model.getName());
                                moduleCode = model.getCode();
                            }
                        }
                    } else {
                        txtTitle.setText(moduleName);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ReleaseActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ReleaseActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void release(final String isPublish) {
        String pic = "";

        for (String s : listPicUrl) {
            pic = pic + s + "||";
        }

        JSONObject object = new JSONObject();
        try {
            object.put("title", edtTitle.getText().toString().trim());
            object.put("content", edtContent.getText().toString().trim());
            object.put("pic", pic);
            object.put("plateCode", moduleCode);
            object.put("publisher", userInfoSp.getString("userId", null));
            object.put("isPublish", isPublish);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_610110, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                if (isPublish.equals("0")) {
                    Toast.makeText(ReleaseActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReleaseActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                }
                finish();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ReleaseActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ReleaseActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void draftToNote() {
        String pic = "";

        for (String s : listPicUrl) {
            pic = pic + s + "||";
        }

        JSONObject object = new JSONObject();
        try {
            object.put("code", draftCode);
            object.put("title", edtTitle.getText().toString().trim());
            object.put("content", edtContent.getText().toString().trim());
            object.put("pic", pic);
            object.put("plateCode", moduleCode);
            object.put("publisher", userInfoSp.getString("userId", null));
            object.put("isPublish", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_610111, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(ReleaseActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ReleaseActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ReleaseActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void choosePhoto(View view) {

        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(this).inflate(R.layout.popup_release, null);

        TextView txtPhotograph = (TextView) mView
                .findViewById(R.id.txt_photograph);
        TextView txtAlbum = (TextView) mView
                .findViewById(R.id.txt_album);
        TextView txtCancel = (TextView) mView
                .findViewById(R.id.txt_releasePopup_cancel);
        TextView txtTitle = (TextView) mView
                .findViewById(R.id.txt_title);

        txtTitle.setText("选择店铺封面");

        LinearLayout dismiss = (LinearLayout) mView.findViewById(R.id.quxiao);

        final PopupWindow popupWindow = new PopupWindow(mView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setTouchable(true);

        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        txtTitle.setText("选择封面");

        dismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        txtAlbum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 调用android的图库
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
                popupWindow.dismiss();
            }
        });

        txtPhotograph.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_CAMARA_IMAGE);

                popupWindow.dismiss();
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.corners_share));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, Gravity.BOTTOM);

    }

    private void tip() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("是否保存至草稿箱?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        release("0");
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).show();
    }

    private boolean releaseCheck() {
        if (edtTitle.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请填写帖子标题", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtContent.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请填写帖子内容", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (moduleCode.equals("")) {
            Toast.makeText(this, "请选择板块", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    private boolean draftCheck() {
        if (!moduleCode.equals("")) {
            if (!edtTitle.getText().toString().trim().equals("")) {
                // 已填写标题
                return true;
            }
            if (!edtContent.getText().toString().trim().equals("")) {
                // 已填写内容
                return true;
            }
        }
        return false;

    }

    public void getNote() {
        JSONObject object = new JSONObject();
        try {
            object.put("code", draftCode);
            object.put("commStatus", "");
            object.put("userId", userInfoSp.getString("userId", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post(Constants.CODE_610132, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);


                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<NoteModel>() {
                    }.getType());

                    setView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ReleaseActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ReleaseActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView() {
        edtTitle.setText(model.getTitle());
        edtContent.setText(model.getContent());
        txtTitle.setText(model.getPlateName());
        moduleCode = model.getPlateCode();

        for (String url : model.getPicArr()) {
            listPic.add(url);
            listPicUrl.add(url);
        }
        recyclerViewAdapter.notifyDataSetChanged();

    }

    private void initEmoji(){
        fragments.clear();

        if (emotions.size() % 28 == 0) {
            emojiPageCount = emotions.size() / 28;
        } else {
            emojiPageCount = emotions.size() / 28 + 1;
        }

        for (int i = 0; i < emojiPageCount; i++) {
            if(i<6){
                // 初始化指示器
                listIndicator.get(i).setVisibility(View.VISIBLE);
            }
            //初始化每一个fragment
            EmojiFragment ef = EmojiFragment.newInstance(i, true, emotions);
            fragments.add(ef);
        }

        pageAdapter.notifyDataSetChanged();

    }

}
