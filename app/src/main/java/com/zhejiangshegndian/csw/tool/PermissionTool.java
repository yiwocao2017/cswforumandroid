package com.zhejiangshegndian.csw.tool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.zhejiangshegndian.csw.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeiQ on 2017/5/9.
 */

public class PermissionTool {

    private static Context context;
    private static PermissionTool mPermission;

    private static String[] permissions;

    public static final int PERMISSON_REQUESTCODE = 0;

    //私有的构造方法，防止随意创建实例
    private PermissionTool(){

    }

    /**
     * new创建实例
     *
     * @return mPermission
     * 返回一定是mPermission本身，这样才能继续调用别的方法，也就是所谓的链式结构
     */
    public static PermissionTool permission(Context c, String[] p){
        context = c;
        permissions = p;
        mPermission = new PermissionTool();

        return mPermission;
    }

    public PermissionTool callback(PermissionCallback callback){
        checkPermissions(callback, permissions);
        return this;
    }

    /**
     * 检查权限是否已申请
     * @param
     * @since 2.5.0
     *
     */
    private static void checkPermissions(PermissionCallback callback,String... permissions){
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions((Activity) context,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);

        }else {
            callback.onSuccess();
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     *
     */
    private static List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(context,
                    perm) != PackageManager.PERMISSION_GRANTED) {
                needRequestPermissonList.add(perm);
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, perm)) {
                    needRequestPermissonList.add(perm);
                }
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否所有的权限都已经授权
     * @param grantResults
     * @return
     * @since 2.5.0
     *
     */
    public static boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     *
     */
    public static void showMissingPermissionDialog(final String permissionName, final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.notifyTitle);
        builder.setMessage("当前应用缺少必要的"+permissionName+"权限。\n\n请点击\"设置\"-\"权限\"-打开所需权限。");

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.cancell,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                });

        builder.setPositiveButton(R.string.setting,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     *  启动应用的设置
     *
     * @since 2.5.0
     *
     */
    private static void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    public interface PermissionCallback{
        void onSuccess();
//        void onFailure();
    }

}
