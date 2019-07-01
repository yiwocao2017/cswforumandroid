package com.zhejiangshegndian.csw.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.activity.ReleaseActivity;
import com.zhejiangshegndian.csw.activity.SearchActivity;
import com.zhejiangshegndian.csw.tool.SignInTool;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by LeiQ on 2017//3.
 */

public class ForumFragment extends Fragment {

    @InjectView(R.id.layout_search)
    LinearLayout layoutSearch;
    @InjectView(R.id.txt_youliao)
    TextView txtYouliao;
    @InjectView(R.id.line_youliao)
    View lineYouliao;
    @InjectView(R.id.txt_luntan)
    TextView txtLuntan;
    @InjectView(R.id.line_luntan)
    View lineLuntan;
    @InjectView(R.id.layout_release)
    LinearLayout layoutRelease;
    @InjectView(R.id.layout_forum)
    FrameLayout layoutForum;
    private View view;

    // 论坛
    private ForumLTFragment ltFragment;
    // 有料
    private ForumYLFragment ylFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forum, null);
        ButterKnife.inject(this, view);

        setSelect(1);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.layout_search, R.id.txt_youliao, R.id.txt_luntan, R.id.layout_release})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;

            case R.id.txt_youliao:
                setSelect(1);
                break;

            case R.id.txt_luntan:
                setSelect(0);
                break;

            case R.id.layout_release:
                if(SignInTool.isSignIn(getActivity())){
                    startActivity(new Intent(getActivity(), ReleaseActivity.class));
                }
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
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        // 把图片设置为亮的
        // 设置内容区域
        switch (i) {
            case 0:
                if (ltFragment == null) {
                    ltFragment = new ForumLTFragment();
                    transaction.add(R.id.layout_forum, ltFragment);
                } else {
                    transaction.show(ltFragment);
                }
                lineLuntan.setVisibility(View.VISIBLE);
                txtLuntan.setTextColor(getResources().getColor(R.color.font_white));
                break;
            case 1:
                if (ylFragment == null) {
                    ylFragment = new ForumYLFragment();
                    transaction.add(R.id.layout_forum, ylFragment);
                } else {
                    transaction.show(ylFragment);

                }
                lineYouliao.setVisibility(View.VISIBLE);
                txtYouliao.setTextColor(getResources().getColor(R.color.font_white));
                break;

            default:
                break;
        }

        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (ltFragment != null) {
            transaction.hide(ltFragment);
        }
        if (ylFragment != null) {
            transaction.hide(ylFragment);
        }
    }

    /**
     * 切换图片至暗色
     */
    public void resetImgs() {
        txtLuntan.setTextColor(getResources().getColor(R.color.font_gray_e49d9e));
        txtYouliao.setTextColor(getResources().getColor(R.color.font_gray_e49d9e));

        lineLuntan.setVisibility(View.GONE);
        lineYouliao.setVisibility(View.GONE);
    }
}
