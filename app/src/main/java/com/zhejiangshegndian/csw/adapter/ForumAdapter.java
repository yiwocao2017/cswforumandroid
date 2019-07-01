package com.zhejiangshegndian.csw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.model.ForumModel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by LeiQ on 2017/4/18.
 */

public class ForumAdapter extends BaseAdapter {

    private List<ForumModel> list;
    private Context context;
    private ViewHolder holder;

    public ForumAdapter(Context context, List<ForumModel> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_forum, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder = (ViewHolder) view.getTag();

        setView(i);

        return view;
    }

    private void setView(int i) {

        if(list.get(i).isSelect()){
            holder.txtName.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.txtName.setTextColor(context.getResources().getColor(R.color.font_red));
        }else {
            holder.txtName.setBackgroundColor(context.getResources().getColor(R.color.intervalColor));
            holder.txtName.setTextColor(context.getResources().getColor(R.color.font_gray));
        }

        holder.txtName.setText(list.get(i).getName());

    }

    static class ViewHolder {
        @InjectView(R.id.txt_name)
        TextView txtName;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
