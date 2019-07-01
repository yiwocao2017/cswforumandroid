package com.zhejiangshegndian.csw.tool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.zhejiangshegndian.csw.activity.SignInActivity;

/**
 * Created by dell1 on 2016/12/15.
 */

public class SignInTool {

    static SharedPreferences userInfoSp;

    public static boolean isSignIn(Context context){
        userInfoSp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        if (userInfoSp.getString("userId", null) != null) {
            return true;
        } else {
            context.startActivity(new Intent(context, SignInActivity.class).putExtra("log","tip"));
        }
        return false;
    }

    /**
     *
     * @param context
     * @return false:未登录,true:已登陆
     */
    public static boolean getSignInState(Context context){
        userInfoSp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        if (userInfoSp.getString("userId", null) != null) {
            return true;
        } else {
            return false;
        }

    }

}
