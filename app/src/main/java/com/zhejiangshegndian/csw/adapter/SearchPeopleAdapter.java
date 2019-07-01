package com.zhejiangshegndian.csw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.holder.TipViewHolder;
import com.zhejiangshegndian.csw.model.PersonalModel;
import com.zhejiangshegndian.csw.tool.ImageTool;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by LeiQ on 2017/4/14.
 */

public class SearchPeopleAdapter extends BaseAdapter {

    private Context context;
    private ViewHolder holder;
    private TipViewHolder tipHolder;
    private List<PersonalModel> list;

    public SearchPeopleAdapter(Context context, List<PersonalModel> list) {
        this.list = list;
        this.context = context;

    }

    @Override
    public int getCount() {
        if(list.size() == 0){
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
            tipHolder.txtTip.setText("没有相关用户");
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_people, null);
            holder = new ViewHolder(view);

            setView(i);
        }

        return view;
    }

    private void setView(int i) {
        ImageTool.glide(context,list.get(i).getUserExt().getPhoto(),holder.imgPhoto);
        holder.txtName.setText(list.get(i).getNickname());
    }

    static class ViewHolder {
        @InjectView(R.id.img_photo)
        CircleImageView imgPhoto;
        @InjectView(R.id.txt_name)
        TextView txtName;
        @InjectView(R.id.txt_fans)
        TextView txtFans;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
