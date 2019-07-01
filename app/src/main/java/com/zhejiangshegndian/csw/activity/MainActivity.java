package com.zhejiangshegndian.csw.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.controller.EaseUI;
import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.fragment.BeeFragment;
import com.zhejiangshegndian.csw.fragment.ForumFragment;
import com.zhejiangshegndian.csw.fragment.MyFragment;
import com.zhejiangshegndian.csw.fragment.NewsFragment;
import com.zhejiangshegndian.csw.fragment.VideoFragment;
import com.zhejiangshegndian.csw.model.MenuModel;
import com.zhejiangshegndian.csw.model.VideoModel;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.ImageTool;
import com.zhejiangshegndian.csw.tool.SignInTool;
import com.zhejiangshegndian.csw.tool.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends MyBaseActivity {

    @InjectView(R.id.layout_fragment)
    FrameLayout layoutFragment;
    @InjectView(R.id.img_news)
    ImageView imgNews;
    @InjectView(R.id.txt_news)
    TextView txtNews;
    @InjectView(R.id.layout_news)
    LinearLayout layoutNews;
    @InjectView(R.id.img_forum)
    ImageView imgForum;
    @InjectView(R.id.txt_forum)
    TextView txtForum;
    @InjectView(R.id.layout_forum)
    LinearLayout layoutForum;
    @InjectView(R.id.layout_bee)
    RelativeLayout layoutBee;
    @InjectView(R.id.img_vidio)
    ImageView imgVidio;
    @InjectView(R.id.txt_vidio)
    TextView txtVidio;
    @InjectView(R.id.layout_vidio)
    LinearLayout layoutVidio;
    @InjectView(R.id.img_my)
    ImageView imgMy;
    @InjectView(R.id.txt_my)
    TextView txtMy;
    @InjectView(R.id.layout_my)
    LinearLayout layoutMy;
    @InjectView(R.id.img_bee)
    ImageView imgBee;
    @InjectView(R.id.txt_unread)
    TextView txtUnread;

    public static MainActivity INSTANCE;


    private Fragment newsFragment;
    private Fragment forumFragment;
    private Fragment beeFragment;
    private Fragment vidioFragment;
    private MyFragment myFragment;

    // menuList
    private List<MenuModel> list;

    // 视频连接
    private String videoUrl = "";

    // 未读消息数
    public int unreadNum;

    int EMSignOutFlag = 101010;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 101010){
//                signOutTip();

                // 实例化Intent
                Intent intent = new Intent();
                // 设置Intent的action属性
                intent.setAction("com.yiwocao.android.citynet.receiver.LogoutReceiver");
                // 发出广播
                sendBroadcast(intent);
            } else if(msg.what == 0){
                txtUnread.setVisibility(View.GONE);
            } else {
                txtUnread.setVisibility(View.VISIBLE);
                txtUnread.setText(msg.what+"");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        INSTANCE = this;

        // 注册环信聊天消息监听
        EMClient.getInstance().chatManager().addMessageListener(new EMChatMessageListtener());
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());

        inits();
        getMenu();

    }

    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getVideo();
        if(SignInTool.getSignInState(this)){
            checkConversation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 移除环信聊天消息监听
        EMClient.getInstance().chatManager().removeMessageListener(new EMChatMessageListtener());
    }

    private void inits() {
        list = new ArrayList<>();

    }

    @OnClick({R.id.layout_news, R.id.layout_forum, R.id.layout_bee, R.id.layout_vidio, R.id.layout_my})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_news:
                setSelect(0);
                break;

            case R.id.layout_forum:
                setSelect(1);
                break;

            case R.id.layout_bee:
                if (SignInTool.isSignIn(MainActivity.this)) {
                    startActivity(new Intent(MainActivity.this, ReleaseActivity.class));
                }

                break;

            case R.id.layout_vidio:
                System.out.println("videoUrl="+videoUrl);
                if(videoUrl.equals("")){
                    setSelect(3);
                }else {
                    startActivity(new Intent(MainActivity.this, WebActivity.class)
                            .putExtra("url",videoUrl));
                }
                break;

            case R.id.layout_my:
                if (SignInTool.isSignIn(MainActivity.this)) {
                    setSelect(4);
                }
                break;
        }
    }

    /**
     * 选择Fragment
     *
     * @param i
     */
    public void setSelect(int i) {
        resetImgs();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        // 把图片设置为亮的
        // 设置内容区域
        switch (i) {
            case 0:
                if (newsFragment == null) {
                    newsFragment = new NewsFragment();
                    transaction.add(R.id.layout_fragment, newsFragment);
                } else {
                    transaction.show(newsFragment);
                }

                for (MenuModel model : list) {
                    if (model.getOrderNo().equals("1")) {
                        txtNews.setText(model.getName());
                        txtNews.setTextColor(getResources().getColor(R.color.font_red));
                        if (model.getPic().split("\\|\\|").length > 1) {
                            ImageTool.glide(MainActivity.this, model.getPic().split("\\|\\|")[1], imgNews);
                        } else {
                            ImageTool.glide(MainActivity.this, model.getPic().split("\\|\\|")[0], imgNews);
                        }

                    }
                }

                break;
            case 1:
                if (forumFragment == null) {
                    forumFragment = new ForumFragment();
                    transaction.add(R.id.layout_fragment, forumFragment);
                } else {
                    transaction.show(forumFragment);

                }

                for (MenuModel model : list) {
                    if (model.getOrderNo().equals("2")) {
                        txtForum.setText(model.getName());
                        txtForum.setTextColor(getResources().getColor(R.color.font_red));
                        if (model.getPic().split("\\|\\|").length > 1) {
                            ImageTool.glide(MainActivity.this, model.getPic().split("\\|\\|")[1], imgForum);
                        } else {
                            ImageTool.glide(MainActivity.this, model.getPic().split("\\|\\|")[0], imgForum);
                        }

                    }
                }

                break;
            case 2:
                if (beeFragment == null) {
                    beeFragment = new BeeFragment();
                    transaction.add(R.id.layout_fragment, beeFragment);
                } else {
                    transaction.show(beeFragment);
                }
                break;

            case 3:
                if (vidioFragment == null) {
                    vidioFragment = new VideoFragment();
                    transaction.add(R.id.layout_fragment, vidioFragment);
                } else {
                    transaction.show(vidioFragment);
                }

                for (MenuModel model : list) {
                    if (model.getOrderNo().equals("4")) {
                        txtVidio.setText(model.getName());
                        txtVidio.setTextColor(getResources().getColor(R.color.font_red));
                        if (model.getPic().split("\\|\\|").length > 1) {
                            ImageTool.glide(MainActivity.this, model.getPic().split("\\|\\|")[1], imgVidio);
                        } else {
                            ImageTool.glide(MainActivity.this, model.getPic().split("\\|\\|")[0], imgVidio);
                        }

                    }
                }

                break;


            case 4:
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    transaction.add(R.id.layout_fragment, myFragment);
                } else {
                    transaction.show(myFragment);
                }

                for (MenuModel model : list) {
                    if (model.getOrderNo().equals("5")) {
                        txtMy.setText(model.getName());
                        txtMy.setTextColor(getResources().getColor(R.color.font_red));
                        if (model.getPic().split("\\|\\|").length > 1) {
                            ImageTool.glide(MainActivity.this, model.getPic().split("\\|\\|")[1], imgMy);
                        } else {
                            ImageTool.glide(MainActivity.this, model.getPic().split("\\|\\|")[0], imgMy);
                        }

                    }
                }

                break;

            default:
                break;
        }

        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (newsFragment != null) {
            transaction.hide(newsFragment);
        }
        if (forumFragment != null) {
            transaction.hide(forumFragment);
        }
        if (beeFragment != null) {
            transaction.hide(beeFragment);
        }
        if (vidioFragment != null) {
            transaction.hide(vidioFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }


    /**
     * 切换图片至暗色
     */
    public void resetImgs() {
        for (MenuModel model : list) {
            switch (model.getOrderNo()) {
                case "1": // 头条
                    txtNews.setText(model.getName());
                    txtNews.setTextColor(getResources().getColor(R.color.font_gray_707070));
                    ImageTool.glide(MainActivity.this, model.getPic().split("\\|\\|")[0], imgNews);
                    break;

                case "2": // 有料
                    txtForum.setText(model.getName());
                    txtForum.setTextColor(getResources().getColor(R.color.font_gray_707070));
                    ImageTool.glide(MainActivity.this, model.getPic().split("\\|\\|")[0], imgForum);
                    break;

                case "3": // 小蜜
                    ImageTool.glide(MainActivity.this, model.getPic().split("\\|\\|")[0], imgBee);
                    break;

                case "4": // 视频
                    txtVidio.setText(model.getName());
                    txtVidio.setTextColor(getResources().getColor(R.color.font_gray_707070));
                    ImageTool.glide(MainActivity.this, model.getPic().split("\\|\\|")[0], imgVidio);
                    break;

                case "5": // 我的
                    txtMy.setText(model.getName());
                    txtMy.setTextColor(getResources().getColor(R.color.font_gray_707070));
                    ImageTool.glide(MainActivity.this, model.getPic().split("\\|\\|")[0], imgMy);
                    break;
            }
        }

    }

    private void getMenu() {
        JSONObject object = new JSONObject();
        try {
            object.put("companyCode", citySp.getString("cityCode", null));
            object.put("orderColumn", "order_no");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_610087, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    Gson gson = new Gson();
                    List<MenuModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<MenuModel>>() {
                    }.getType());

                    list.clear();
                    list.addAll(lists);

                    // 初始化主页fragment
                    setSelect(0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(MainActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(MainActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getVideo() {
        JSONObject object = new JSONObject();
        try {
            object.put("name", "");
            object.put("status", "2");
            object.put("parentCode", "");
            object.put("companyCode", citySp.getString("cityCode", null));
            object.put("start", "1");
            object.put("limit", "10");
            object.put("orderColumn", "order_no");
            object.put("orderDir", "asc");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_610055, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    List<VideoModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<VideoModel>>() {
                    }.getType());

                    System.out.println("lists.size()="+lists.size());
                    if(lists.size()==1){
                        System.out.println("lists.get(0).getUrl()="+lists.get(0).getUrl());
                        videoUrl = lists.get(0).getUrl();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(MainActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(MainActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkConversation() {

        unreadNum = 0;

        // 获取所有会话
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        for (EMConversation conversation : conversations.values()) {
            // conversationId : 聊天用户ID
            if (conversation != null) {
                System.out.println("conversation.getUnreadMsgCount()="+conversation.conversationId());
                System.out.println("conversation.getUnreadMsgCount()="+conversation.getUnreadMsgCount());
                if (conversation.getUnreadMsgCount() > 0) {
                    unreadNum = unreadNum + conversation.getUnreadMsgCount();
                }
            }

        }

        Message message = new Message();
        message.what = unreadNum;
        handler.sendMessage(message);

        if(myFragment != null){
            myFragment.showUnread(unreadNum);
        }

    }

    private class EMChatMessageListtener implements EMMessageListener{

        @Override
        public void onMessageReceived(List<EMMessage> list) {
            checkConversation();
            EaseUI.getInstance().getNotifier().onNewMsg(list.get(0));
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    }

    /**
     *
     * 环信连接状态监听
     */
    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("_____________________error="+error);


                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                        Toast.makeText(MainActivity.this, "帐号已被移除", Toast.LENGTH_SHORT).show();
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        signOutEM();
                    }

                }
            });
        }
    }

    private void signOutEM() {

        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                Message msg = handler.obtainMessage();
                msg.what = EMSignOutFlag;
                handler.sendMessage(msg);
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub
                System.out.println("signOutEM________onProgress");


            }

            @Override
            public void onError(int code, String message) {
                System.out.println("signOutEM________onError");
                System.out.println("退出失败: " + code + ", " + message);

                if(code == 212){
                    Message msg = handler.obtainMessage();
                    msg.what = EMSignOutFlag;
                    handler.sendMessage(msg);
                }



            }
        });
    }

    private void signOutTip() {
        new AlertDialog.Builder(this).setTitle("警告")
                .setMessage("帐号在其他设备登录,请重新登录!")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

//                        finish();
//                        System.exit(0);
                        startActivity(new Intent(MainActivity.this, SignInActivity.class).putExtra("EMSignOut",true));
                    }
                }).show();
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            tip();
        }
        return false;
    }

    private void tip() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("您确定要退出城市网吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton("取消", null).show();
    }
}
