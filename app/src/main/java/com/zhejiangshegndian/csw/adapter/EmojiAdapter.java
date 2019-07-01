package com.zhejiangshegndian.csw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.model.EmotionsModel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EmojiAdapter extends BaseAdapter {

    private List<EmotionsModel> modelList;
    private Context context;
    private ViewHolder holder;

    public EmojiAdapter(Context context, List<EmotionsModel> modelList) {
        this.modelList = modelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        System.out.println("modelList.size()="+modelList.size());
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_emoji, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        setView(position);

        return convertView;
    }

    private void setView(int position) {
        try {
            String png = modelList.get(position).getPng();

            int emoji = context.getResources().getIdentifier(png.split("\\.")[0], "mipmap" , context.getPackageName());
            holder.imgEmoji.setImageResource(emoji);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    static class ViewHolder {
        @InjectView(R.id.img_emoji)
        ImageView imgEmoji;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
