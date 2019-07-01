package com.zhejiangshegndian.csw.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.fragment.CommentReceiveFragment;
import com.zhejiangshegndian.csw.fragment.CommentSendFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MyCommentActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_send)
    TextView txtSend;
    @InjectView(R.id.line_send)
    View lineSend;
    @InjectView(R.id.txt_receive)
    TextView txtReceive;
    @InjectView(R.id.line_receive)
    View lineReceive;
    @InjectView(R.id.layout_comment)
    FrameLayout layoutComment;

    private CommentSendFragment sendFragment;
    private CommentReceiveFragment receiveFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment);
        ButterKnife.inject(this);
        setSelect(0);
    }

    @OnClick({R.id.layout_back, R.id.txt_send, R.id.txt_receive})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_send:
                setSelect(0);
                break;

            case R.id.txt_receive:
                setSelect(1);
                break;
        }
    }

    /**
     * 选择Fragment
     *
     * @param i
     */
    public void setSelect(int i) {
        resetImgs();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        // 把图片设置为亮的
        // 设置内容区域
        switch (i) {
            case 0:
                if (sendFragment == null) {
                    sendFragment = new CommentSendFragment();
                    transaction.add(R.id.layout_comment, sendFragment);
                } else {
                    transaction.show(sendFragment);
                }
                lineSend.setVisibility(View.VISIBLE);
                txtSend.setTextColor(getResources().getColor(R.color.font_white));
                break;

            case 1:
                if (receiveFragment == null) {
                    receiveFragment = new CommentReceiveFragment();
                    transaction.add(R.id.layout_comment, receiveFragment);
                } else {
                    transaction.show(receiveFragment);

                }
                lineReceive.setVisibility(View.VISIBLE);
                txtReceive.setTextColor(getResources().getColor(R.color.font_white));
                break;

            default:
                break;
        }

        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (sendFragment != null) {
            transaction.hide(sendFragment);
        }
        if (receiveFragment != null) {
            transaction.hide(receiveFragment);
        }
    }

    /**
     * 切换图片至暗色
     */
    public void resetImgs() {
        txtSend.setTextColor(getResources().getColor(R.color.font_gray_e49d9e));
        txtReceive.setTextColor(getResources().getColor(R.color.font_gray_e49d9e));

        lineSend.setVisibility(View.GONE);
        lineReceive.setVisibility(View.GONE);
    }
}