package com.zhejiangshegndian.csw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.holder.TipViewHolder;
import com.zhejiangshegndian.csw.model.VideoModel;
import com.zhejiangshegndian.csw.tool.ImageTool;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by LeiQ on 2017/3/8.
 */

public class VideoAdapter extends BaseAdapter {

    private Context context;
    private List<VideoModel> list;
    private ViewHolder viewHolder;
    private TipViewHolder tipHolder;

    public VideoAdapter(Context context, List<VideoModel> list) {
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
        if (list.size() == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.item_tip, null);
            tipHolder = new TipViewHolder(view);
            tipHolder.txtTip.setText("暂无视频");
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_video, null);
            viewHolder = new ViewHolder(view);

            setView(i);
        }

        return view;
    }

    private void setView(int i) {
        viewHolder.txtName.setText(list.get(i).getName());
        ImageTool.glide(context, list.get(i).getPic(),viewHolder.imgVideo);
    }

    static class ViewHolder {
        @InjectView(R.id.img_video)
        ImageView imgVideo;
        @InjectView(R.id.txt_name)
        TextView txtName;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
