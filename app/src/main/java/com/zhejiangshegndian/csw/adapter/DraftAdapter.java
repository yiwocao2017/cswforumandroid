package com.zhejiangshegndian.csw.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.activity.ReleaseActivity;
import com.zhejiangshegndian.csw.holder.TipViewHolder;
import com.zhejiangshegndian.csw.model.NoteModel;
import com.zhejiangshegndian.csw.tool.DateTool;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by LeiQ on 2017/3/8.
 */

public class DraftAdapter extends BaseAdapter {

    private Context context;
    private List<NoteModel> list;
    private ViewHolder viewHolder;
    private TipViewHolder tipHolder;

    public DraftAdapter(Context context, List<NoteModel> list) {
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
            tipHolder.txtTip.setText("暂无草稿");
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_draft, null);
            viewHolder = new ViewHolder(view);

            setView(i);
        }
        return view;
    }

    private void setView(final int i) {
        viewHolder.txtTime.setText("存稿时间: "+
                DateTool.formatDate(list.get(i).getPublishDatetime(),"MM月dd日 HH:mm"));
        viewHolder.txtTitle.setText("标题: "+ list.get(i).getTitle());
        viewHolder.txtContent.setText("内容: "+ list.get(i).getContent());

        viewHolder.txtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ReleaseActivity.class)
                        .putExtra("code",list.get(i).getCode()));
            }
        });

    }

    static class ViewHolder {
        @InjectView(R.id.txt_time)
        TextView txtTime;
        @InjectView(R.id.txt_title)
        TextView txtTitle;
        @InjectView(R.id.txt_content)
        TextView txtContent;
        @InjectView(R.id.txt_send)
        TextView txtSend;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
