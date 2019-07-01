package com.zhejiangshegndian.csw.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.holder.TipViewHolder;
import com.zhejiangshegndian.csw.model.EmotionsModel;
import com.zhejiangshegndian.csw.model.NoteModel;
import com.zhejiangshegndian.csw.tool.BitMapUtil;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.DateTool;
import com.zhejiangshegndian.csw.tool.ImageTool;
import com.zhejiangshegndian.csw.tool.SaxTool;
import com.zhejiangshegndian.csw.tool.SignInTool;
import com.zhejiangshegndian.csw.tool.WxUtil;
import com.zhejiangshegndian.csw.tool.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.zhejiangshegndian.csw.tool.Constants.SHARE;

/**
 * Created by LeiQ on 2017/4/10.
 */

public class NoteAdapter extends BaseAdapter {

    private Context context;
    private ViewHolder holder;
    private List<NoteModel> list;
    private TipViewHolder tipHolder;
    private SharedPreferences userInfoSp;

    private List<EmotionsModel> emotions;

    public NoteAdapter(Context context, List<NoteModel> list) {
        this.list = list;
        this.context = context;

        userInfoSp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        try {
            emotions = SaxTool.sax2xml(context.getResources().getAssets().open("emoji.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
//        if (list.size() == 0) {
//            return 1;
//        }
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
//        if (list.size() == 0) {
//            view = LayoutInflater.from(context).inflate(R.layout.item_tip, null);
//            tipHolder = new TipViewHolder(view);
//            tipHolder.txtTip.setText("暂无帖子");
//        } else {
//
//        }

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_note, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder = (ViewHolder) view.getTag();
        holder.layoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WxUtil.check(context)) {
                    WxUtil.share(context, view,
                            SHARE + list.get(i).getCode(),
                            list.get(i).getTitle(),
                            list.get(i).getContent(),
                            "");
                }
            }
        });

        holder.layoutPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SignInTool.isSignIn(context)) {
                    praise(i);
                }
            }
        });

        setView(i);

        return view;
    }

    public void setView(int i) {
        ImageTool.photo(context, list.get(i).getPhoto(), holder.imgPhoto);
        holder.txtName.setText(list.get(i).getNickname());
        holder.txtModule.setText(list.get(i).getPlateName() + "模块");
        holder.txtTime.setText(DateTool.formatDate(list.get(i).getPublishDatetime(),
                "yyyy-MM-dd HH:mm:ss"));
        holder.txtTitle.setText(list.get(i).getTitle());
        holder.txtContent.setText(list.get(i).getContent());

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
                        holder.txtContent.setText(spannableString,TextView.BufferType.SPANNABLE);
                    }
                }
            }
        }

        holder.txtAllContent.setVisibility(View.GONE);

        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        if (list.get(i).getPicArr() != null) {
            for (String imageUrl : list.get(i).getPicArr()) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(imageUrl);
                info.setBigImageUrl(imageUrl);
                imageInfo.add(info);
            }
        }
        holder.nineGrid.setAdapter(new NineGridViewClickAdapter(context, imageInfo));

        holder.txtPraiseNum.setText(list.get(i).getSumLike() + "");
        if (list.get(i).getIsDZ().equals("0")) { // 未点赞
            holder.imgPraise.setImageDrawable(context.getResources().getDrawable(R.mipmap.praise));
        } else { // 已点赞
            holder.imgPraise.setImageDrawable(context.getResources().getDrawable(R.mipmap.praise_red));
        }

        holder.txtCommentNum.setText(list.get(i).getSumComment() + "");

        if (list.get(i).getLikeList() != null) {
            if (list.get(i).getLikeList().size() != 0) {
                holder.layoutPraiseUser.setVisibility(View.VISIBLE);
                // 点赞用户昵称集合
                List<String> praiseUserList = new ArrayList<>();
                for (NoteModel.LikeListBean bean : list.get(i).getLikeList()) {
                    praiseUserList.add(bean.getNickname());
                }
                // 拼接评论用户
                String praiseUser = "";
                for (String nickName : praiseUserList) {
                    praiseUser = nickName + "、";
                }
                holder.txtPraiseByWho.setText(praiseUser.substring(0, praiseUser.length() - 1));
            }
        }


        if (list.get(i).getCommentList() != null) {
            if (list.get(i).getCommentList().size() != 0) {
                holder.layoutCommentContent.removeAllViews();
                holder.layoutComment.setVisibility(View.VISIBLE);

                for (int index = 0; index < list.get(i).getCommentList().size(); index++) {
                    if (index < 10) {
                        NoteModel.CommentListBean bean = list.get(i).getCommentList().get(index);
                        TextView commentContent = new TextView(context);
                        commentContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                        commentContent.setText(Html.fromHtml("<font color='#7d0000'>" + bean.getNickname() + "</font>:" + bean.getContent()));
                        holder.layoutCommentContent.addView(commentContent);
                    }
                    if (index == 10) {
                        holder.txtAllComment.setVisibility(View.VISIBLE);
                    }
                }


            }
        }

    }

    private void praise(final int i) {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("postCode", list.get(i).getCode());
            object.put("type", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_610121, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    if (list.get(i).getIsDZ().equals("0")) { // 未点赞，设置为点赞
                        list.get(i).setIsDZ("1");
                        list.get(i).setSumLike(list.get(i).getSumLike()+1);
                    } else { // 已点赞，设置为为点赞
                        list.get(i).setIsDZ("0");
                        list.get(i).setSumLike(list.get(i).getSumLike()-1);
                    }

                    notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(context, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class ViewHolder {
        @InjectView(R.id.img_photo)
        CircleImageView imgPhoto;
        @InjectView(R.id.txt_name)
        TextView txtName;
        @InjectView(R.id.txt_module)
        TextView txtModule;
        @InjectView(R.id.txt_time)
        TextView txtTime;
        @InjectView(R.id.txt_title)
        TextView txtTitle;
        @InjectView(R.id.txt_content)
        TextView txtContent;
        @InjectView(R.id.txt_allContent)
        TextView txtAllContent;
        @InjectView(R.id.nineGrid)
        NineGridView nineGrid;
        @InjectView(R.id.layout_share)
        LinearLayout layoutShare;
        @InjectView(R.id.img_praise)
        ImageView imgPraise;
        @InjectView(R.id.txt_praiseNum)
        TextView txtPraiseNum;
        @InjectView(R.id.layout_praise)
        LinearLayout layoutPraise;
        @InjectView(R.id.txt_commentNum)
        TextView txtCommentNum;
        @InjectView(R.id.layout_doComment)
        LinearLayout layoutDoComment;
        @InjectView(R.id.txt_praise_user_num)
        TextView txtPraiseUserNum;
        @InjectView(R.id.txt_praiseByWho)
        TextView txtPraiseByWho;
        @InjectView(R.id.layout_praiseUser)
        LinearLayout layoutPraiseUser;
        @InjectView(R.id.layout_comment_content)
        LinearLayout layoutCommentContent;
        @InjectView(R.id.layout_comment)
        LinearLayout layoutComment;
        @InjectView(R.id.txt_allComment)
        TextView txtAllComment;
        @InjectView(R.id.layout_info)
        LinearLayout layoutInfo;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
