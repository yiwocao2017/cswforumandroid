package com.zhejiangshegndian.csw.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.activity.ModuleActivity;
import com.zhejiangshegndian.csw.activity.StoreActivity;
import com.zhejiangshegndian.csw.activity.WebActivity;
import com.zhejiangshegndian.csw.adapter.ModuleAdapter;
import com.zhejiangshegndian.csw.model.GridModel;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ModuleFragment extends Fragment {

	private View view;
	private GridView gv;
	private int index = -1;
	private FragmentActivity context;
	private List<GridModel> modelList;
	private TextView no;

	private SharedPreferences userInfoSp;
	private SharedPreferences appConfigSp;

	public static ModuleFragment newInstance(int index, ArrayList<GridModel> modelList) {
		ModuleFragment gf = new ModuleFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("index", index);
		bundle.putParcelableArrayList("model", modelList);
		gf.setArguments(bundle);
		return gf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getActivity().getSharedPreferences("appConfig", Context.MODE_PRIVATE);

		if (view == null) {
			context = getActivity();

			Bundle bundle = getArguments();
			index = bundle.getInt("index");
			modelList = bundle.getParcelableArrayList("model");

			List<GridModel> newModels;
			int last = 8 * index + 8;
			if (last >= modelList.size()) {
				newModels = modelList.subList((8 * index), (modelList.size()));

			} else {
				newModels = modelList.subList((8 * index), (last));
			}

			view = LayoutInflater.from(context).inflate(R.layout.fragment_grid,
					container, false);
			gv = (GridView) view.findViewById(R.id.gridview);

			// 这里重新开辟一个地址空间，来保存list，否则会报ConcurrentModificationException错误
			final ArrayList<GridModel> text = new ArrayList<GridModel>();
			text.addAll(newModels);
			gv.setAdapter(new ModuleAdapter(getActivity(), text));

			gv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
                    System.out.println("text.get(position).getUrl()="+text.get(position).getUrl());
                    if(text.get(position).getUrl().indexOf("http") != -1){
                        // url 包含"http"
                        startActivity(new Intent(getActivity(), WebActivity.class));
                    }else{
                        String str[] = text.get(position).getUrl().split(",");
                        System.out.println("text.get(position).getUrl()="+text.get(position).getUrl());

						if(str[0].split("\\:")[0].equals("page")){
							switch (str[0].split("\\:")[1]){
								case "mall":
									startActivity(new Intent(getActivity(), StoreActivity.class));
									break;

								case "signin":
									sign();
									break;

								case "board":
									startActivity(new Intent(getActivity(), ModuleActivity.class)
											.putExtra("plateCode",  str[1].split("\\:")[1]));
									break;
							}
						}
                    }


//					startActivity(new Intent(getActivity(), ModuleActivity.class).putExtra("plateCode",text.get(position).getType()));

				}
			});

		} else {
			Log.i("tag", "当前页数是" + index + "view不是null哦");
			ViewGroup root = (ViewGroup) view.getParent();
			if (root != null) {
				root.removeView(view);
			}
		}

		Log.i("tag", "当前页数是" + index);

		return view;
	}

    private void sign() {
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token",""));
            object.put("userId", userInfoSp.getString("userId",""));
            object.put("systemCode", appConfigSp.getString("systemCode",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_805100, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    showSign(true,jsonObject.getDouble("amount"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void onTip(String tip) {
                showSign(false,0);
//                Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(getActivity(), "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSign(boolean isSucceed,double amount) {

        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_sign, null);

        ImageView imgSign = (ImageView) mView.findViewById(R.id.img_sign);
        TextView txtAmount = (TextView) mView.findViewById(R.id.txt_amount);
        LinearLayout layoutAmount = (LinearLayout) mView.findViewById(R.id.layout_amount);

        final PopupWindow popupWindow = new PopupWindow(mView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setTouchable(true);
//        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        if(isSucceed){
            imgSign.setImageResource(R.mipmap.sign_succeed);
            txtAmount.setText((amount/1000)+"");
            layoutAmount.setVisibility(View.VISIBLE);
        }else {
            imgSign.setImageResource(R.mipmap.sign_failure);
        }

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_share));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

}
