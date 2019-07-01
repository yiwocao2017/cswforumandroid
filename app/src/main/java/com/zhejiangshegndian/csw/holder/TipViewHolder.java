package com.zhejiangshegndian.csw.holder;

import android.view.View;
import android.widget.TextView;

import com.zhejiangshegndian.csw.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by LeiQ on 2017/5/2.
 */

public class TipViewHolder {
    @InjectView(R.id.txt_tip)
    public TextView txtTip;

    public TipViewHolder(View view) {
        ButterKnife.inject(this, view);
    }
}
