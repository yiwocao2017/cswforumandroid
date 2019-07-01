package com.zhejiangshegndian.csw.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.activity.CommentActivity;
import com.zhejiangshegndian.csw.activity.ReleaseActivity;
import com.zhejiangshegndian.csw.adapter.EmojiAdapter;
import com.zhejiangshegndian.csw.model.EmotionsModel;
import com.zhejiangshegndian.csw.tool.BitMapUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by LeiQ on 2017/5/24.
 */

public class EmojiFragment extends Fragment {

    @InjectView(R.id.gridview)
    GridView gridview;

    private View view;
    private int index = -1;

    private boolean isRelease;

    ReleaseActivity releaseActivity;
    CommentActivity commentActivity;

    private List<EmotionsModel> modelList;
    private List<EmotionsModel> newModels;
    // 这里重新开辟一个地址空间，来保存list，否则会报ConcurrentModificationException错误
    private List<EmotionsModel> listContent;
    private EmojiAdapter emojiAdapter;

    public static EmojiFragment newInstance(int index, boolean isRelease, List<EmotionsModel> modelList) {
        EmojiFragment ef = new EmojiFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        bundle.putBoolean("isRelease", isRelease);
        bundle.putSerializable("model", (Serializable) modelList);
        ef.setArguments(bundle);
        return ef;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_emoji, container, false);
            ButterKnife.inject(this, view);
            init();
            initGridView();
            initData();
        }else {
            ViewGroup root = (ViewGroup) view.getParent();
            if (root != null) {
                root.removeView(view);
            }
        }
        if(isRelease){
            releaseActivity = (ReleaseActivity) getActivity();
        }else{
            commentActivity = (CommentActivity) getActivity();
        }


        return view;
    }

    private void init() {
        modelList = new ArrayList<>();
        newModels = new ArrayList<>();
        listContent = new ArrayList<>();

        emojiAdapter = new EmojiAdapter(getActivity(), listContent);
    }

    private void initGridView() {
        gridview.setAdapter(emojiAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 2;//图片宽高都为原来的二分之一，即图片为原来的四分之一

                int emoji = getActivity().getResources().getIdentifier(listContent.get(i).getPng().split("\\.")[0], "mipmap" , getActivity().getPackageName());
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),emoji,options);
                ImageSpan imageSpan = new ImageSpan(getActivity(), BitMapUtil.zoomImg(bitmap,60,60));
                SpannableString spannableString = new SpannableString(listContent.get(i).getChs());  //图片所表示的文字
                spannableString.setSpan(imageSpan, 0, spannableString.length(),SpannableString.SPAN_MARK_MARK);
                if(isRelease){
                    releaseActivity.edtContent.append(spannableString);
                }else{
                    commentActivity.edtComment.append(spannableString);
                }
            }
        });
    }

    private void initData(){

        index = getArguments().getInt("index");
        isRelease = getArguments().getBoolean("isRelease");
        modelList = (List<EmotionsModel>) getArguments().getSerializable("model");

        int last = 28 * index + 28;
        if (last >= modelList.size()) {
            newModels = modelList.subList((28 * index), (modelList.size()));

        } else {
            newModels = modelList.subList((28 * index), (last));
        }
        listContent.addAll(newModels);
        System.out.println("listContent.size()="+ listContent.size());
        emojiAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
