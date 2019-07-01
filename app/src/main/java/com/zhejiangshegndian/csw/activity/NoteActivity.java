package com.zhejiangshegndian.csw.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.adapter.AwardUserAdapter;
import com.zhejiangshegndian.csw.adapter.CommentAdapter;
import com.zhejiangshegndian.csw.adapter.PraiseAdapter;
import com.zhejiangshegndian.csw.model.AttentionModel;
import com.zhejiangshegndian.csw.model.AwardUserModel;
import com.zhejiangshegndian.csw.model.CommentModel;
import com.zhejiangshegndian.csw.model.EmotionsModel;
import com.zhejiangshegndian.csw.model.NoteModel;
import com.zhejiangshegndian.csw.model.PraiseUserModel;
import com.zhejiangshegndian.csw.tool.BitMapUtil;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.DateTool;
import com.zhejiangshegndian.csw.tool.ImageTool;
import com.zhejiangshegndian.csw.tool.SaxTool;
import com.zhejiangshegndian.csw.tool.SignInTool;
import com.zhejiangshegndian.csw.tool.Xutil;
import com.zhejiangshegndian.csw.view.RefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.zhejiangshegndian.csw.R.id.img_photo;


public class NoteActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener,AdapterView.OnItemClickListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_note)
    ListView listNote;
    @InjectView(R.id.txt_comment_note)
    TextView txtCommentNote;
    @InjectView(R.id.layout_praise_note)
    LinearLayout layoutPraiseNote;
    @InjectView(R.id.img_praise_note)
    ImageView imgPraiseNote;
    @InjectView(R.id.layout_more)
    LinearLayout layoutMore;
    @InjectView(R.id.layout_refresh)
    RefreshLayout layoutRefresh;

    // 头部View
    private View headView;

    View lineComment;
    View linePraise;

    Button btnAttention;

    GridView gridAward;
    NineGridView nineGrid;

    ImageView imgAward;
    ImageView imgPraise;
    CircleImageView imgPhoto;

    TextView txtTime;
    TextView txtNote;
    TextView txtName;
    TextView txtTitle;
    TextView txtPraise;
    TextView txtBrowse;
    TextView txtComment;
    TextView txtAwardNum;
    TextView txtPraiseNum;
    TextView txtCommentNum;

    LinearLayout layoutBrowse;
    LinearLayout layoutPraise;
    LinearLayout layoutDoComment;

    // 评论 点赞选择状态
    private boolean isAtComment = true;

    // 关注状态 true: 已关注;false: 未关注;
    private boolean isAttention = false;

    private int praisePage = 1;
    private int praisePageSize = 10;
    private PraiseAdapter praiseAdapter;
    private List<PraiseUserModel> praiseList;

    private int commentPage = 1;
    private int commentPageSize = 10;
    private CommentAdapter commentAdapter;
    private List<CommentModel> commentList;

    private String code;
    private NoteModel model;

    private List<AwardUserModel> awardList;
    private AwardUserAdapter awardUserAdapter;

    private List<EmotionsModel> emotions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        ButterKnife.inject(this);

        inits();
//        initEditText();
        initHeadView();
        initListView();
        initRefreshLayout();

        // 阅读帖子详情
        readNote();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isAtComment){
            getComment();
        }else{
            getPraise();
        }
    }

    private void inits() {
        praiseList = new ArrayList<>();
        commentList = new ArrayList<>();
        awardList = new ArrayList<>();

        praiseAdapter = new PraiseAdapter(this, praiseList);
        commentAdapter = new CommentAdapter(this, commentList);
        awardUserAdapter = new AwardUserAdapter(this, awardList);

        code = getIntent().getStringExtra("code");

        try {
            emotions = SaxTool.sax2xml(getResources().getAssets().open("emoji.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void initEditText() {
//        edtComment.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                if (!(s.length() > start)) {
//                    return;
//                }
//                if ('@' == s.charAt(start) && count == 1) {
//                    startActivityForResult(new Intent(NoteActivity.this, LinkManActivity.class), 0);
//                    return;
//                }
//
////                if ((s.charAt(start) == '@') && (s.charAt(start + count - 1) == ' ')) {
////                    if ('@' == s.charAt(start - 1)) {
////                        posterContentEt.getText().delete(start - 1, start);
////                    }
////                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//    }

    private void initHeadView() {
        headView = LayoutInflater.from(this).inflate(R.layout.header_note, null);

        layoutBrowse = (LinearLayout) headView.findViewById(R.id.layout_browse);
        layoutPraise = (LinearLayout) headView.findViewById(R.id.layout_praise);
        layoutDoComment = (LinearLayout) headView.findViewById(R.id.layout_doComment);

        imgAward = (ImageView) headView.findViewById(R.id.img_award);
        imgPraise = (ImageView) headView.findViewById(R.id.img_praise);
        imgPhoto = (CircleImageView) headView.findViewById(img_photo);

        gridAward = (GridView) headView.findViewById(R.id.grid_award);
        nineGrid = (NineGridView) headView.findViewById(R.id.nineGrid);

        txtTime = (TextView) headView.findViewById(R.id.txt_time);
        txtNote = (TextView) headView.findViewById(R.id.txt_note);
        txtName = (TextView) headView.findViewById(R.id.txt_name);
        txtTitle = (TextView) headView.findViewById(R.id.txt_title);
        txtPraise = (TextView) headView.findViewById(R.id.txt_praise);
        txtBrowse = (TextView) headView.findViewById(R.id.txt_browse);
        txtComment = (TextView) headView.findViewById(R.id.txt_comment);
        txtAwardNum = (TextView) headView.findViewById(R.id.txt_awardNum);
        txtPraiseNum = (TextView) headView.findViewById(R.id.txt_praiseNum);
        txtCommentNum = (TextView) headView.findViewById(R.id.txt_commentNum);

        lineComment = headView.findViewById(R.id.line_comment);
        linePraise = headView.findViewById(R.id.line_praise);

        btnAttention = (Button) headView.findViewById(R.id.btn_attention);

        gridAward.setAdapter(awardUserAdapter);

        imgPhoto.setOnClickListener(new HeadViewClickListener());
        imgAward.setOnClickListener(new HeadViewClickListener());
        txtPraise.setOnClickListener(new HeadViewClickListener());
        txtComment.setOnClickListener(new HeadViewClickListener());
        btnAttention.setOnClickListener(new HeadViewClickListener());
        layoutPraise.setOnClickListener(new HeadViewClickListener());
    }

    private void initListView() {
        listNote.addHeaderView(headView);
        listNote.setOnItemClickListener(this);
        listNote.setAdapter(commentAdapter);
    }

    private void initRefreshLayout() {
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        layoutRefresh.setOnRefreshListener(this);
        layoutRefresh.setOnLoadListener(this);

    }

    private void initLine() {
        linePraise.setBackgroundColor(getResources().getColor(R.color.white));
        lineComment.setBackgroundColor(getResources().getColor(R.color.white));
    }


    public class HeadViewClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case img_photo:
                    startActivity(new Intent(NoteActivity.this, PersonActivity.class)
                            .putExtra("userId",model.getPublisher()));
                    break;

                case R.id.btn_attention:
                    if (SignInTool.isSignIn(NoteActivity.this)) {
                        if (isAttention) {
                            noAttention();
                        } else {
                            attention();
                        }
                    }

                    break;

                case R.id.layout_praise:
                    if (SignInTool.isSignIn(NoteActivity.this)) {
                        praise();
                    }
                    break;

                case R.id.img_award:
                    if (SignInTool.isSignIn(NoteActivity.this)) {
                        showAward(view);
                    }
                    break;

                case R.id.txt_comment:
                    initLine();
                    lineComment.setBackgroundColor(getResources().getColor(R.color.font_red));
                    listNote.setAdapter(commentAdapter);
                    isAtComment = true;
                    commentPage = 1;
                    getComment();
                    break;

                case R.id.txt_praise:
                    initLine();
                    linePraise.setBackgroundColor(getResources().getColor(R.color.font_red));
                    listNote.setAdapter(praiseAdapter);
                    isAtComment = false;
                    praisePage = 1;
                    getPraise();

                    break;
            }
        }
    }

    @OnClick({R.id.layout_back, R.id.layout_praise_note, R.id.layout_more, R.id.txt_comment_note})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_praise_note:
                if (SignInTool.isSignIn(NoteActivity.this)) {
                    praise();
                }
                break;

            case R.id.layout_more:
                showOption(view);
                break;

            case R.id.txt_comment_note:
                if (SignInTool.isSignIn(NoteActivity.this)) {
                    startActivity(new Intent(NoteActivity.this, CommentActivity.class)
                            .putExtra("code",code));
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(i > 0){
            if(isAtComment){
                // 弹出帖子评论操作选项
                if(commentList.size()>0){
                    if(commentList.get(i-1).getCommer().equals(userInfoSp.getString("userId",""))){
                        showMyCommentOption(view, commentList.get(i-1).getCode());
                    }else {
                        showCommentOption(view,commentList.get(i-1).getNickname(),commentList.get(i-1).getCode());
                    }
                }

            }else {
                if(praiseList.size() > 0){
                    startActivity(new Intent(NoteActivity.this, PersonActivity.class)
                            .putExtra("userId",praiseList.get(i-1).getTalker()));
                }
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {

        }
    }

    private void showAward(View view) {

        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(this).inflate(R.layout.popup_award, null);

        final EditText edtAward = (EditText) mView.findViewById(R.id.edt_award);
        TextView txtCancel = (TextView) mView.findViewById(R.id.txt_cancel);
        TextView txtConfirm = (TextView) mView.findViewById(R.id.txt_confirm);

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

        edtAward.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        //设置字符过滤
        edtAward.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(".") && dest.toString().length() == 0) {
                    return "0.";
                }
                if (dest.toString().contains(".")) {
                    int index = dest.toString().indexOf(".");
                    int mlength = dest.toString().substring(index).length();
                    if (mlength == 3) {
                        return "";
                    }
                }
                return null;
            }
        }});

        txtCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
            }
        });

        txtConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (edtAward.getText().toString().equals("")) {
                    Toast.makeText(NoteActivity.this, "请填写打赏金额", Toast.LENGTH_SHORT).show();
                } else {
                    if (Double.parseDouble(edtAward.getText().toString().trim()) == 0.0) {
                        Toast.makeText(NoteActivity.this, "打赏金额需要大于0哦", Toast.LENGTH_SHORT).show();
                    } else {
                        award(Double.parseDouble(edtAward.getText().toString().trim()));
                        popupWindow.dismiss();
                    }
                }

            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_share));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    private void showOption(final View view) {

        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(this).inflate(R.layout.popup_note, null);

        TextView txtCollect = (TextView) mView.findViewById(R.id.txt_collect);
        TextView txtCancel = (TextView) mView.findViewById(R.id.txt_cancel);
        final TextView txtReport = (TextView) mView.findViewById(R.id.txt_report);

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

        if(model.getPublisher().equals(userInfoSp.getString("userId",""))){
            txtReport.setText("删除");
        }

        if (model.getIsSC().equals("1")) {
            txtCollect.setText("取消收藏");
        }

        txtCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
            }
        });

        txtReport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
                if(txtReport.getText().equals("举报")){
                    // 举报帖子
                    showReport(view, "1",code);
                }else{
                    // 删除帖子
                    showDelete("1",code);
                }

            }
        });

        txtCollect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                collect(popupWindow);
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_share));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    private void showCommentOption(final View view, final String nickName, final String noteCode) {

        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(this).inflate(R.layout.popup_note_comment, null);

        TextView txtReply = (TextView) mView.findViewById(R.id.txt_reply);
        TextView txtCancel = (TextView) mView.findViewById(R.id.txt_cancel);
        TextView txtReport = (TextView) mView.findViewById(R.id.txt_report);

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


        txtCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
            }
        });

        txtReport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
                // 举报评论
                showReport(view, "2", noteCode);
            }
        });

        txtReply.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(NoteActivity.this, CommentActivity.class)
                        .putExtra("code",code).putExtra("nickName",nickName));
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_share));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    private void showMyCommentOption(final View view, final String noteCode) {

        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(this).inflate(R.layout.popup_note_my_comment, null);

        TextView txtDelete = (TextView) mView.findViewById(R.id.txt_delete);

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
                // 删除评论
                showDelete("2",noteCode);
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_share));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    private void showReport(View view, final String reportType, final String code) {

        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(this).inflate(R.layout.popup_report, null);

        final EditText edtReport = (EditText) mView.findViewById(R.id.edt_report);
        TextView txtCancel = (TextView) mView.findViewById(R.id.txt_cancel);
        TextView txtConfirm = (TextView) mView.findViewById(R.id.txt_confirm);

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


        txtCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
            }
        });

        txtConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (edtReport.getText().toString().equals("")) {
                    Toast.makeText(NoteActivity.this, "请填写举报理由", Toast.LENGTH_SHORT).show();
                } else {
                    report(popupWindow, edtReport.getText().toString().trim(),reportType,code);
                }

            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_share));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    private void showDelete(final String type, final String code) {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("您确定要删除吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete(type, code);
                    }
                }).setNegativeButton("取消", null).show();
    }

    public void readNote() {
        JSONObject object = new JSONObject();
        try {
            object.put("postCode", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post(Constants.CODE_610120, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                getNote();
                getAwardList();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(NoteActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(NoteActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(NoteActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                if(type.equals("1")){
                    finish();
                }else{
                    commentPage = 1;
                    getComment();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(NoteActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(NoteActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getNote() {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("commStatus", "BD");
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
                    isAttention();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(NoteActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(NoteActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView() {
        ImageTool.photo(this, model.getPhoto(), imgPhoto);

        txtTitle.setText(model.getTitle());
        txtNote.setText(model.getContent());

        Pattern pattern = Pattern.compile("\\[(\\S+?)\\]");
        Matcher matcher = pattern.matcher(model.getContent());
        SpannableString spannableString = new SpannableString(model.getContent());
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String str =  model.getContent().substring(start,end);
            if(str != null){
                for(EmotionsModel model : emotions){
                    if(str.equals(model.getChs())){
                        int emoji = getResources().getIdentifier(model.getPng().split("\\.")[0], "mipmap" , getPackageName());
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),emoji);
                        ImageSpan imageSpan = new ImageSpan(this, BitMapUtil.zoomImg(bitmap,60,60));
                        spannableString.setSpan(imageSpan, start, end,SpannableString.SPAN_MARK_MARK);
                        txtNote.setText(spannableString,TextView.BufferType.SPANNABLE);
                    }
                }
            }
        }

        txtName.setText(model.getNickname());
        txtTime.setText(DateTool.formatDate(model.getPublishDatetime(),"yyyy年MM月dd日 HH:mm"));

        if (!userInfoSp.getString("userId", "").equals(model.getPublisher())) {
            btnAttention.setVisibility(View.VISIBLE);
        }

        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        if (model.getPicArr() != null) {
            for (String imageUrl : model.getPicArr()) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(imageUrl);
                info.setBigImageUrl(imageUrl);
                imageInfo.add(info);
            }
        }
        nineGrid.setAdapter(new NineGridViewClickAdapter(NoteActivity.this, imageInfo));

        txtBrowse.setText(model.getSumRead() + "");
        txtPraiseNum.setText(model.getSumLike() + "");
        txtCommentNum.setText(model.getSumComment() + "");

        if (model.getIsDZ().equals("0")) { // 未点赞
            Glide.with(this).load(R.mipmap.praise).into(imgPraise);
            Glide.with(this).load(R.mipmap.praise_note).into(imgPraiseNote);
        } else { // 已点赞
            Glide.with(this).load(R.mipmap.praise_red).into(imgPraise);
//            Glide.with(this).load(R.mipmap.praise_note).into(imgPraiseNote);
        }

        txtAwardNum.setText(model.getSumReward() + "");

    }

    public void getAwardList() {
        JSONObject object = new JSONObject();
        try {
            object.put("postCode", code);
            object.put("start", "1");
            object.put("limit", "27");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_610142, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    ArrayList<AwardUserModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<AwardUserModel>>() {
                    }.getType());

                    awardList.clear();
                    awardList.addAll(lists);
                    awardUserAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(NoteActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(NoteActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getComment() {
        JSONObject object = new JSONObject();
        try {
            object.put("postCode", code);
            object.put("content", "");
            object.put("status", "BD");
            object.put("start", commentPage + "");
            object.put("limit", commentPageSize + "");
            object.put("orderDir", "");
            object.put("orderColumn", "");
//            object.put("companyCode", citySp.getString("cityCode",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post(Constants.CODE_610133, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    ArrayList<CommentModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<CommentModel>>() {
                    }.getType());

                    if (commentPage == 1) {
                        commentList.clear();
                    }
                    commentList.addAll(lists);
                    commentAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(NoteActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(NoteActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getPraise() {
        JSONObject object = new JSONObject();
        try {
            object.put("postCode", code);
            object.put("userId", "");
            object.put("start", praisePage + "");
            object.put("limit", praisePageSize + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post(Constants.CODE_610141, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    ArrayList<PraiseUserModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<PraiseUserModel>>() {
                    }.getType());

                    if (praisePage == 1) {
                        praiseList.clear();
                    }

                    praiseList.addAll(lists);
                    praiseAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(NoteActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(NoteActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void isAttention() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", "");
            object.put("mobile", "");
            object.put("toUser", model.getPublisher());
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
                        btnAttention.setText("取消关注");
                    } else {
                        isAttention = false;
                        btnAttention.setText("+关注");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(NoteActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(NoteActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void attention() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", ""));
            object.put("toUser", model.getPublisher());
            object.put("token", userInfoSp.getString("token", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post(Constants.CODE_805080, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(NoteActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                isAttention();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(NoteActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(NoteActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void noAttention() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", ""));
            object.put("toUser", model.getPublisher());
            object.put("token", userInfoSp.getString("token", ""));
            object.put("systemCode", appConfigSp.getString("systemCode", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post(Constants.CODE_805081, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(NoteActivity.this, "取消关注成功", Toast.LENGTH_SHORT).show();
                isAttention();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(NoteActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(NoteActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void award(double award) {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", ""));
            object.put("amount", award * 1000);
            object.put("postCode", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post(Constants.CODE_610122, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(NoteActivity.this, "打赏成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(NoteActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(NoteActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void praise() {
        JSONObject object = new JSONObject();
        try {
            object.put("type", "1");
            object.put("postCode", code);
            object.put("userId", userInfoSp.getString("userId", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post(Constants.CODE_610121, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                getNote();

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(NoteActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(NoteActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void collect(final PopupWindow popupWindow) {
        JSONObject object = new JSONObject();
        try {
            object.put("type", "2");
            object.put("postCode", code);
            object.put("userId", userInfoSp.getString("userId", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post(Constants.CODE_610121, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                popupWindow.dismiss();
                if (model.getIsSC().equals("0")) {
                    Toast.makeText(NoteActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NoteActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                }
                getNote();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(NoteActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(NoteActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void report(final PopupWindow popupWindow, String reportNote, String reportType,String code) {
        JSONObject object = new JSONObject();
        try {
            object.put("type", reportType);
            object.put("code", code);
            object.put("reportNote", reportNote);
            object.put("reporter", userInfoSp.getString("userId", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_610113, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                popupWindow.dismiss();
                Toast.makeText(NoteActivity.this, "举报成功，我们会及时处理", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(NoteActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(NoteActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onRefresh() {
        layoutRefresh.postDelayed(new Runnable() {

            @Override
            public void run() {
                layoutRefresh.setRefreshing(false);
                readNote();

                if(isAtComment){
                    commentPage = 1;
                    getComment();
                }else{
                    praisePage = 1;
                    getPraise();
                }

            }
        }, 1500);
    }

    @Override
    public void onLoad() {
        layoutRefresh.postDelayed(new Runnable() {

            @Override
            public void run() {
                layoutRefresh.setLoading(false);
                if(isAtComment){
                    commentPage = commentPage +1;
                    getComment();
                }else{
                    praisePage = praisePage +1;
                    getPraise();
                }

            }
        }, 1500);
    }

}
