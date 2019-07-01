package com.zhejiangshegndian.csw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.model.AwardUserModel;
import com.zhejiangshegndian.csw.tool.ImageTool;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by LeiQ on 2017/4/19.
 */

public class AwardUserAdapter extends BaseAdapter {

    private Context context;
    private ViewHolder holder;
    private List<AwardUserModel> list;

    public AwardUserAdapter(Context context, List<AwardUserModel> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_award_user, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ImageTool.photo(context, list.get(i).getPhoto(), holder.imgPhoto);

        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.img_photo)
        CircleImageView imgPhoto;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
