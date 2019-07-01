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
import com.zhejiangshegndian.csw.fragment.PayFragment;
import com.zhejiangshegndian.csw.fragment.TakeFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MyGoodActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_title)
    TextView txtTitle;
    @InjectView(R.id.txt_pay)
    TextView txtPay;
    @InjectView(R.id.txt_take)
    TextView txtTake;
    @InjectView(R.id.line_pay)
    View linePay;
    @InjectView(R.id.line_take)
    View lineTake;
    @InjectView(R.id.layout_good)
    FrameLayout layoutGood;

    private PayFragment payFragment;
    private TakeFragment takeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_good);
        ButterKnife.inject(this);

        setSelect(0);
    }

    @OnClick({R.id.layout_back, R.id.txt_take, R.id.txt_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_pay:
                setSelect(0);
                break;

            case R.id.txt_take:
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
                if (payFragment == null) {
                    payFragment = new PayFragment();
                    transaction.add(R.id.layout_good, payFragment);
                } else {
                    transaction.show(payFragment);
                }
                txtPay.setTextColor(getResources().getColor(R.color.font_red));
                linePay.setBackgroundColor(getResources().getColor(R.color.font_red));
                break;

            case 1:
                if (takeFragment == null) {
                    takeFragment = new TakeFragment();
                    transaction.add(R.id.layout_good, takeFragment);
                } else {
                    transaction.show(takeFragment);

                }
                txtPay.setTextColor(getResources().getColor(R.color.font_red));
                lineTake.setBackgroundColor(getResources().getColor(R.color.font_red));
                break;

            default:
                break;
        }

        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (payFragment != null) {
            transaction.hide(payFragment);
        }
        if (takeFragment != null) {
            transaction.hide(takeFragment);
        }
    }

    /**
     * 切换图片至暗色
     */
    public void resetImgs() {
        txtPay.setTextColor(getResources().getColor(R.color.font_gray));
        txtTake.setTextColor(getResources().getColor(R.color.font_gray));

        linePay.setBackgroundColor(getResources().getColor(R.color.lineColor));
        lineTake.setBackgroundColor(getResources().getColor(R.color.lineColor));
    }
}
