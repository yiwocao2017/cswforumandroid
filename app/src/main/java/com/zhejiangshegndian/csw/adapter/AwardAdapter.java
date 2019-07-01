package com.zhejiangshegndian.csw.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.model.AwardModel;
import com.zhejiangshegndian.csw.tool.DateTool;
import com.zhejiangshegndian.csw.tool.MoneyUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by LeiQ on 2017/3/8.
 */

public class AwardAdapter extends BaseAdapter {

    private List<AwardModel> list;
    private Context context;
    private ViewHolder viewHolder;

    public AwardAdapter(Context context, List<AwardModel> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_award, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        setView(i);

        return view;
    }

    private void setView(int i) {
        viewHolder.txtTitle.setText(list.get(i).getBizNote());
        viewHolder.txtTime.setText(DateTool.formatDate(list.get(i).getCreateDatetime(),"yyyy-MM-dd"));
        viewHolder.txtAward.setText(MoneyUtil.moneyFormatDouble(list.get(i).getTransAmount()));
    }

    static class ViewHolder {
        @InjectView(R.id.txt_title)
        TextView txtTitle;
        @InjectView(R.id.txt_time)
        TextView txtTime;
        @InjectView(R.id.txt_award)
        TextView txtAward;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
