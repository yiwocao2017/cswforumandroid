package com.zhejiangshegndian.csw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.holder.TipViewHolder;
import com.zhejiangshegndian.csw.model.PraiseUserModel;
import com.zhejiangshegndian.csw.tool.DateTool;
import com.zhejiangshegndian.csw.tool.ImageTool;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by LeiQ on 2017/3/8.
 */

public class PraiseNoteAdapter extends BaseAdapter {

    private Context context;
    private ViewHolder holder;
    private TipViewHolder tipHolder;
    private List<PraiseUserModel> list;

    public PraiseNoteAdapter(Context context, List<PraiseUserModel> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (list.size() == 0) {
            return 1;
        }
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
            tipHolder.txtTip.setText("暂无点赞");
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_praise, null);
            holder = new ViewHolder(view);
            view.setTag(holder);

            setView(i);
        }


        return view;
    }

    private void setView(int i) {
        ImageTool.photo(context, list.get(i).getPhoto(), holder.imgPhoto);
        holder.txtName.setText(list.get(i).getNickname());
        holder.txtTime.setText(DateTool.formatDate(list.get(i).getTalkDatetime(),"MM月dd日 HH:mm"));
        holder.txtTitle.setText(list.get(i).getPostTitle());
        holder.txtContent.setText(list.get(i).getPostContent());
    }

    static class ViewHolder {
        @InjectView(R.id.img_photo)
        CircleImageView imgPhoto;
        @InjectView(R.id.txt_name)
        TextView txtName;
        @InjectView(R.id.txt_time)
        TextView txtTime;
        @InjectView(R.id.txt_title)
        TextView txtTitle;
        @InjectView(R.id.txt_content)
        TextView txtContent;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
