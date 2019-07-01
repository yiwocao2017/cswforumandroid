package com.zhejiangshegndian.csw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.model.ModuleModel;
import com.zhejiangshegndian.csw.tool.ImageTool;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by LeiQ on 2017/4/18.
 */

public class ForumModuleAdapter extends BaseAdapter {

    private List<ModuleModel> list;
    private Context context;
    private ViewHolder holder;

    public ForumModuleAdapter(Context context, List<ModuleModel> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_forum_module, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder = (ViewHolder) view.getTag();

        setView(i);

        return view;
    }

    private void setView(int i) {

        ImageTool.glide(context,list.get(i).getPic(),holder.imgPicture);

        holder.txtName.setText(list.get(i).getName());
        holder.txtPraise.setText(list.get(i).getAllLikeNum()+"");
        holder.txtComment.setText(list.get(i).getAllCommentNum()+"");

    }

    static class ViewHolder {
        @InjectView(R.id.img_picture)
        ImageView imgPicture;
        @InjectView(R.id.txt_name)
        TextView txtName;
        @InjectView(R.id.txt_praise)
        TextView txtPraise;
        @InjectView(R.id.txt_comment)
        TextView txtComment;
        @InjectView(R.id.txt_into)
        TextView txtInto;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
