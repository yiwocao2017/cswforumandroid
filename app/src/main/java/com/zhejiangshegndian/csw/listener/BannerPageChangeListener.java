package com.zhejiangshegndian.csw.listener;

import android.support.v4.view.ViewPager;

import com.zhejiangshegndian.csw.view.RefreshLayout;

/**
 * Created by LeiQ on 2017/4/14.
 */

public class BannerPageChangeListener implements ViewPager.OnPageChangeListener {

    RefreshLayout refreshLayout;

    public BannerPageChangeListener(RefreshLayout refreshLayout){
        this.refreshLayout = refreshLayout;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);
    }

    private void enableDisableSwipeRefresh(boolean b) {
        if (refreshLayout != null) {
            refreshLayout.setEnabled(b);
        }
    }
}
