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
import com.zhejiangshegndian.csw.model.GoodsModel;
import com.zhejiangshegndian.csw.tool.ImageTool;
import com.zhejiangshegndian.csw.tool.MoneyUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by LeiQ on 2017/4/18.
 */

public class StoreAdapter extends BaseAdapter {

    private List<GoodsModel> list;
    private Context context;
    private ViewHolder holder;
    private TipViewHolder tipHolder;

    public StoreAdapter(Context context, List<GoodsModel> list) {
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
        if (list.size() == 0){
            view = LayoutInflater.from(context).inflate(R.layout.item_tip, null);
            tipHolder = new TipViewHolder(view);
            tipHolder.txtTip.setText("暂无商品");
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_store, null);
            holder = new ViewHolder(view);
            view.setTag(holder);

            setView(i);
        }

        return view;
    }

    private void setView(int i) {

        ImageTool.glide(context,list.get(i).getAdvPic(),holder.imgGood);
        holder.txtName.setText(list.get(i).getName());
        holder.txtPrice.setText(MoneyUtil.moneyFormatDouble(list.get(i).getPrice2())+"赏金");
    }

    static class ViewHolder {
        @InjectView(R.id.img_good)
        ImageView imgGood;
        @InjectView(R.id.txt_name)
        TextView txtName;
        @InjectView(R.id.txt_price)
        TextView txtPrice;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
