package com.zhejiangshegndian.csw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.zhejiangshegndian.csw.MyBaseActivity;
import com.zhejiangshegndian.csw.R;
import com.zhejiangshegndian.csw.adapter.ConversationAdapter;
import com.zhejiangshegndian.csw.model.AttentionModel;
import com.zhejiangshegndian.csw.model.ConversationModel;
import com.zhejiangshegndian.csw.tool.Constants;
import com.zhejiangshegndian.csw.tool.Xutil;
import com.zhejiangshegndian.csw.view.RefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MessageActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, AdapterView.OnItemClickListener  {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.layout_what)
    LinearLayout layoutWhat;
    @InjectView(R.id.list_people)
    ListView listPeople;
    @InjectView(R.id.layout_refresh)
    RefreshLayout layoutRefresh;

    LinearLayout layoutPraise;
    LinearLayout layoutReferMe;
    LinearLayout layoutComment;
    LinearLayout layoutMessage;

    private View headView;

    private ConversationAdapter adapter;
    private List<ConversationModel> list;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.inject(this);

        inits();
        initHeadView();
        initListView();
        initRefreshLayout();

        // 注册环信聊天消息监听
        EMClient.getInstance().chatManager().addMessageListener(new EMChatMessageListtener());

    }

    @Override
    protected void onResume() {
        super.onResume();
//        getPeople();
        checkConversation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 移除环信聊天消息监听
        EMClient.getInstance().chatManager().removeMessageListener(new EMChatMessageListtener());
    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new ConversationAdapter(this, list);
    }

    private void initHeadView() {
        headView = LayoutInflater.from(this).inflate(R.layout.item_message, null);
        layoutPraise = (LinearLayout) headView.findViewById(R.id.layout_praise);
        layoutReferMe = (LinearLayout) headView.findViewById(R.id.layout_refer_me);
        layoutComment = (LinearLayout) headView.findViewById(R.id.layout_comment);
        layoutMessage = (LinearLayout) headView.findViewById(R.id.layout_message);

        layoutPraise.setOnClickListener(new HeaderViewOnClickListener());
        layoutReferMe.setOnClickListener(new HeaderViewOnClickListener());
        layoutComment.setOnClickListener(new HeaderViewOnClickListener());
        layoutMessage.setOnClickListener(new HeaderViewOnClickListener());
    }

    private void initListView() {
        listPeople.addHeaderView(headView);
        listPeople.setOnItemClickListener(this);
        listPeople.setAdapter(adapter);
    }

    private void initRefreshLayout() {
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        layoutRefresh.setOnRefreshListener(this);
//        layoutRefresh.setOnLoadListener(this);

    }

    private class HeaderViewOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.layout_praise:
                    startActivity(new Intent(MessageActivity.this, PraiseActivity.class));
                    break;

                case R.id.layout_refer_me:
                    startActivity(new Intent(MessageActivity.this, ReferActivity.class));
                    break;

                case R.id.layout_comment:
                    startActivity(new Intent(MessageActivity.this, MyCommentActivity.class));
                    break;

                case R.id.layout_message:
                    startActivity(new Intent(MessageActivity.this, SystemMessageActivity.class));
                    break;

            }
        }
    }

    @OnClick({R.id.layout_back, R.id.layout_what})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_what:
                startActivity(new Intent(MessageActivity.this, ChatListActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(list.size() >0){
            Intent intent = new Intent(MessageActivity.this, ChatActivity.class);
            intent.putExtra("nickName", list.get(i-1).getNickname());
            intent.putExtra("myPhoto", userInfoSp.getString("photo", ""));
            intent.putExtra("myName", userInfoSp.getString("nickName", ""));
            intent.putExtra("otherPhoto", list.get(i-1).getPhoto());
            intent.putExtra(EaseConstant.EXTRA_USER_ID, list.get(i-1).getConversationId());
            intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
            startActivity(intent);
        }
    }

    private void getPeople() {
        JSONObject object = new JSONObject();
        try {
            // 我关注了谁
            object.put("userId", userInfoSp.getString("userId", ""));
            object.put("mobile", "");
            // 谁关注了这个人
            object.put("toUser", "");
            object.put("token", userInfoSp.getString("token", ""));
            object.put("systemCode", appConfigSp.getString("systemCode", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(Constants.CODE_805091, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONArray jsonArray = new JSONArray(result);

                    Gson gson = new Gson();
                    ArrayList<AttentionModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<AttentionModel>>() {
                    }.getType());



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(MessageActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(MessageActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkConversation() {

        list.clear();

        // 获取所有会话
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        for (EMConversation conversation : conversations.values()){

            ConversationModel model = new ConversationModel();
            // unreadMsgCount : 未读消息数
            model.setUnRead(conversation.getUnreadMsgCount());
            // conversationId : 聊天用户ID
            model.setConversationId(conversation.conversationId());
            model.setNickname(conversation.getLastMessage().getStringAttribute("nickName",""));
            model.setPhoto(conversation.getLastMessage().getStringAttribute("photo",""));

            list.add(model);
        }

//        List<String> conversationList = new ArrayList<>();
//        for (String conversationId : conversationList){
//            // 遍历关注人集合，比对conversationId与用户ID
//            for (AttentionModel model : lists) {
//                if(conversationId.toUpperCase().equals(model.getUserId())){
//                    // 获取未读消息
//                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(model.getUserId());
//                    // 判断是否添加未读消息数
//                    if (conversation != null) {
//                        if (conversation.getUnreadMsgCount() > 0) {
//                            model.setUnRead(conversation.getUnreadMsgCount());
//                        }
//                    }
//                    list.add(model);
//                }
//            }
//        }
        handler.sendEmptyMessage(0);

    }

    private class EMChatMessageListtener implements EMMessageListener {

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

    @Override
    public void onRefresh() {
        layoutRefresh.postDelayed(new Runnable() {

            @Override
            public void run() {
                layoutRefresh.setRefreshing(false);
//                getPeople();
                checkConversation();
                // 更新数据
                // 更新完后调用该方法结束刷新

            }

        }, 1500);
    }

    @Override
    public void onLoad() {
        layoutRefresh.postDelayed(new Runnable() {

            @Override
            public void run() {
                layoutRefresh.setLoading(false);
//                getPeople();
            }
        }, 1500);
    }
}
