package com.zhejiangshegndian.csw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.model.OrderModel;
import com.zhejiangshegndian.csw.tool.ImageTool;
import com.zhejiangshegndian.csw.tool.MoneyUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by LeiQ on 2017/4/18.
 */

public class OrderAdapter extends BaseAdapter {

    private List<OrderModel> list;
    private Context context;
    private ViewHolder holder;

    public OrderAdapter(Context context, List<OrderModel> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_store, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder = (ViewHolder) view.getTag();

        setView(i);

        return view;
    }

    private void setView(int i) {

        ImageTool.glide(context,list.get(i).getProductOrderList().get(0).getProduct().getAdvPic(),holder.imgGood);
        holder.txtName.setText(list.get(i).getProductOrderList().get(0).getProduct().getName());
        holder.txtPrice.setText(MoneyUtil.moneyFormatDouble(list.get(i).getPayAmount2())+"赏金");
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
