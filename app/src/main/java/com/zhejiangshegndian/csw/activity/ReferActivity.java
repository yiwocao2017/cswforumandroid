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
import com.zhejiangshegndian.csw.fragment.ReferCommentFragment;
import com.zhejiangshegndian.csw.fragment.ReferNoteFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ReferActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_note)
    TextView txtNote;
    @InjectView(R.id.line_note)
    View lineNote;
    @InjectView(R.id.txt_comment)
    TextView txtComment;
    @InjectView(R.id.line_comment)
    View lineComment;
    @InjectView(R.id.layout_refer)
    FrameLayout layoutRefer;

    private ReferNoteFragment noteFragment;
    private ReferCommentFragment commentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);
        ButterKnife.inject(this);
        setSelect(0);
    }

    @OnClick({R.id.layout_back, R.id.txt_note, R.id.txt_comment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_note:
                setSelect(0);
                break;

            case R.id.txt_comment:
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
                if (noteFragment == null) {
                    noteFragment = new ReferNoteFragment();
                    transaction.add(R.id.layout_refer, noteFragment);
                } else {
                    transaction.show(noteFragment);
                }
                lineNote.setVisibility(View.VISIBLE);
                txtNote.setTextColor(getResources().getColor(R.color.font_white));
                break;

            case 1:
                if (commentFragment == null) {
                    commentFragment = new ReferCommentFragment();
                    transaction.add(R.id.layout_refer, commentFragment);
                } else {
                    transaction.show(commentFragment);

                }
                lineComment.setVisibility(View.VISIBLE);
                txtComment.setTextColor(getResources().getColor(R.color.font_white));
                break;

            default:
                break;
        }

        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (noteFragment != null) {
            transaction.hide(noteFragment);
        }
        if (commentFragment != null) {
            transaction.hide(commentFragment);
        }
    }

    /**
     * 切换图片至暗色
     */
    public void resetImgs() {
        txtNote.setTextColor(getResources().getColor(R.color.font_gray_e49d9e));
        txtComment.setTextColor(getResources().getColor(R.color.font_gray_e49d9e));

        lineNote.setVisibility(View.GONE);
        lineComment.setVisibility(View.GONE);
    }
}
