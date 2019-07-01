package com.zhejiangshegndian.csw.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.holder.TipViewHolder;
import com.zhejiangshegndian.csw.model.CommentModel;
import com.zhejiangshegndian.csw.model.EmotionsModel;
import com.zhejiangshegndian.csw.tool.BitMapUtil;
import com.zhejiangshegndian.csw.tool.DateTool;
import com.zhejiangshegndian.csw.tool.ImageTool;
import com.zhejiangshegndian.csw.tool.SaxTool;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by LeiQ on 2017/3/8.
 */

public class CommentNoteAdapter extends BaseAdapter {

    private Context context;
    private ViewHolder holder;
    private TipViewHolder tipHolder;
    private List<CommentModel> list;
    private List<EmotionsModel> emotions;

    public CommentNoteAdapter(Context context, List<CommentModel> list) {
        this.list = list;
        this.context = context;

        try {
            emotions = SaxTool.sax2xml(context.getResources().getAssets().open("emoji.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            tipHolder.txtTip.setText("暂无评论");
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_comment, null);
            holder = new ViewHolder(view);
            view.setTag(holder);

            setView(i);
        }


        return view;
    }

    private void setView(int i) {
        ImageTool.photo(context, list.get(i).getPhoto(), holder.imgPhoto);
        holder.txtName.setText(list.get(i).getNickname());
        holder.txtTime.setText(DateTool.formatDate(list.get(i).getCommDatetime(),"MM月dd日 HH:mm"));
        holder.txtTitle.setText(list.get(i).getPost().getTitle());
        holder.txtContent.setText(list.get(i).getPost().getContent());
        holder.txtComment.setText(list.get(i).getContent());

        Pattern pattern = Pattern.compile("\\[(\\S+?)\\]");
        Matcher matcher = pattern.matcher(list.get(i).getContent());
        SpannableString spannableString = new SpannableString(list.get(i).getContent());
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String str =  list.get(i).getContent().substring(start,end);
            if(str != null){
                for(EmotionsModel model : emotions){
                    if(str.equals(model.getChs())){

                        int emoji = context.getResources().getIdentifier(model.getPng().split("\\.")[0], "mipmap" , context.getPackageName());
                        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),emoji);
                        ImageSpan imageSpan = new ImageSpan(context, BitMapUtil.zoomImg(bitmap,60,60));
                        spannableString.setSpan(imageSpan, start, end,SpannableString.SPAN_MARK_MARK);
                        holder.txtComment.setText(spannableString,TextView.BufferType.SPANNABLE);
                    }
                }
            }
        }

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
        @InjectView(R.id.layout_info)
        LinearLayout layoutInfo;
        @InjectView(R.id.txt_comment)
        TextView txtComment;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
