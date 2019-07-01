package com.zhejiangshegndian.csw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.adapter.PagerAdapter;
import com.zhejiangshegndian.csw.fragment.EmojiFragment;
import com.zhejiangshegndian.csw.model.EmotionsModel;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.SaxTool;
import com.zhejiangshegndian.csw.tool.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CommentActivity extends MyBaseActivity {

    @InjectView(R.id.txt_cancel)
    TextView txtCancel;
    @InjectView(R.id.txt_confirm)
    TextView txtConfirm;
    @InjectView(R.id.edt_comment)
    public EditText edtComment;
    @InjectView(R.id.txt_textNum)
    TextView txtTextNum;
    @InjectView(R.id.layout_call)
    LinearLayout layoutCall;
    @InjectView(R.id.layout_emoji)
    LinearLayout layoutEmoji;
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

    private String code;
    private String nickName;

    // 表情
    private ArrayList<Fragment> fragments;
    private PagerAdapter pageAdapter;
    private int emojiPageCount;
    private List<TextView> listIndicator;

    // xml
    private SaxTool saxTool;
    private List<EmotionsModel> emotions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.inject(this);

        init();
        initEditText();
        initViewPage();
    }

    private void init() {
        emotions = new ArrayList<>();
        fragments = new ArrayList<>();
        listIndicator = new ArrayList<>();

        pageAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);

        code = getIntent().getStringExtra("code");
        nickName = getIntent().getStringExtra("nickName");

        if (nickName != null) {
            String call = "@" + nickName + " ";
            edtComment.setText(call);
            Editable etext = edtComment.getText();
            Selection.setSelection(etext, etext.length());
        }
    }

    private void initEditText() {
        edtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtTextNum.setText(editable.length() + "");
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

    @OnClick({R.id.txt_cancel, R.id.txt_confirm, R.id.layout_call, R.id.layout_emoji})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_cancel:
                finish();
                break;

            case R.id.txt_confirm:
                comment();
                break;

            case R.id.layout_call:
                startActivityForResult(new Intent(CommentActivity.this, LinkManActivity.class), 0);
                break;

            case R.id.layout_emoji:
                if (layoutEmojis.getVisibility() == View.VISIBLE) {
                    layoutEmojis.setVisibility(View.GONE);

                } else {
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            if (data != null) {
                String nickName = data.getStringExtra("nickName");
                String call = "@" + nickName + " ";

//                edtComment.setText(Html.fromHtml(edtComment.getText().toString().trim() + "<font color='#d23e3e'>"+call+"</font>"));
                edtComment.setText(edtComment.getText().toString().trim() + call);
                Editable etext = edtComment.getText();
                Selection.setSelection(etext, etext.length());
            }
        }

    }

    public void comment() {
        JSONObject object = new JSONObject();
        try {
            object.put("type", "1");
            object.put("content", edtComment.getText().toString().trim());
            object.put("parentCode", code);
            object.put("commer", userInfoSp.getString("userId", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_610112, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(CommentActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(CommentActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(CommentActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

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
            EmojiFragment ef = EmojiFragment.newInstance(i, false,emotions);
            fragments.add(ef);
        }

        pageAdapter.notifyDataSetChanged();

    }
}
