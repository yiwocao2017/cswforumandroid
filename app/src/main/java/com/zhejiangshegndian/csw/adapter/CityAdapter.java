package com.zhejiangshegndian.csw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.model.CityModel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by LeiQ on 2017/3/9.
 */

public class CityAdapter extends BaseAdapter {

    private Context mContext;
    private List<CityModel> list = null;

    public CityAdapter(Context mContext, List<CityModel> list) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_city, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        // 根据position获取分类的首字母的Char ASCII值
        int section = getAsciiForPosition(position);

        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionFroAscii(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(list.get(position).getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        viewHolder.tvTitle.setText(this.list.get(position).getName());
        return view;
    }

    /**
     * 根据位置转成首字母的ASCII
     *
     * @param position
     * @return
     */
    public int getAsciiForPosition(int position) {
        int a = list.get(position).getSortLetters().charAt(0);
        return a;
    }

    /**
     * 根据字母的ASCII获取位置
     *
     * @param ascii
     * @return
     */
    public int getPositionFroAscii(int ascii) {
        for (int i = 0; i < list.size(); i++) {
            String abc = list.get(i).getSortLetters();
            char firstChar = abc.toUpperCase().charAt(0);
            if (firstChar == ascii)
                return i;
        }
        return -1;
    }

    static class ViewHolder {
        @InjectView(R.id.catalog)
        TextView tvLetter;
        @InjectView(R.id.title)
        TextView tvTitle;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
