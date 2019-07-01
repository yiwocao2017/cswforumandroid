package com.zhejiangshegndian.csw;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.lzy.ninegrid.NineGridView;
import com.zhejiangshegndian.csw.loader.NineGridViewImageLoader;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by LeiQ on 2017/3/2.
 */

public class MyApplication extends Application {

    public static Context applicationContext;
    public static MyApplication instance;
    private SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        instance = this;
        initAppConfig();

        initJpush();
        initXUtil();
        initEaseUI();
        initNineGridView();

    }

    //返回
    public static Context getContext(){
        return applicationContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initAppConfig() {
        preferences = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();// 获取编辑器
        editor.putString("systemCode", "CD-CCSW000008");
        // 微信第一次登陆标识，false第一次，true非第一次
        editor.putBoolean("wxSignFlag", false);
        editor.commit();
    }

    /**
     * 初始化极光
     */
    private void initJpush(){
        JPushInterface.init(applicationContext);
        JPushInterface.setDebugMode(true);
        JPushInterface.setLatestNotificationNumber(this, 3);
    }

    /**
     * 初始化xUtil
     */
    private void initXUtil() {
        x.Ext.init(this);
        // 开启debug会影响性能
        x.Ext.setDebug(org.xutils.BuildConfig.DEBUG);
    }

    /**
     * 初始化EaseUI
     */
    private void initEaseUI() {
        EaseUI.getInstance().init(this,null);
        EMClient.getInstance().setDebugMode(true);
        EaseUI.getInstance().setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                return null;
            }
        });
    }

    /**
     * 初始化NineGridView
     */
    private void initNineGridView() {
        NineGridView.setImageLoader(new NineGridViewImageLoader());
    }
}
