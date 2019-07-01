package com.zhejiangshegndian.csw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.model.NoteModel;
import com.zhejiangshegndian.csw.tool.DateTool;
import com.zhejiangshegndian.csw.tool.ImageTool;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by LeiQ on 2017/3/7.
 */

public class NewsAdapter extends BaseAdapter {

    private List<NoteModel> list;
    private Context context;
    private ViewHolder holder;

    public NewsAdapter(Context context, List<NoteModel> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_news, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setView(i);

        return view;
    }

    private void setView(int i) {
        holder.txtTitle.setText(list.get(i).getTitle());
        holder.txtBrowse.setText(list.get(i).getSumRead()+"");
        holder.txtDate.setText(DateTool.formatDate(list.get(i).getPublishDatetime(),"yyyy-MM-dd"));

        if(list.get(i).getPicArr() != null){
            if(list.get(i).getPicArr().size() > 0){
                ImageTool.glide(context,list.get(i).getPicArr().get(0),holder.imgPhoto);
            }
        }
    }

    static class ViewHolder {
        @InjectView(R.id.img_photo)
        ImageView imgPhoto;
        @InjectView(R.id.txt_title)
        TextView txtTitle;
        @InjectView(R.id.txt_date)
        TextView txtDate;
        @InjectView(R.id.txt_browse)
        TextView txtBrowse;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
