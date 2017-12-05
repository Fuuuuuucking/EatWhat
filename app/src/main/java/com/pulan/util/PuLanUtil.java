package com.pulan.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.pulan.app.MyApplication;

/**
 * Created by Spectrum on 16.1.29.
 */
public class PuLanUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取单个App图标
     **/
    public Drawable getAppIcon(String packageName) throws PackageManager.NameNotFoundException {
        Drawable icon = MyApplication.getInstance().getPackageManager().getApplicationIcon(packageName);
        return icon;
    }

    /**
     * 获取单个App名称
     **/
    public static String getAppName(String packageName) throws PackageManager.NameNotFoundException {
        ApplicationInfo appInfo = MyApplication.getInstance().getPackageManager().getApplicationInfo(packageName, 0);
        String appName = MyApplication.getInstance().getPackageManager().getApplicationLabel(appInfo).toString();
        return appName;
    }

    /**
     * 获取单个App版本号
     **/
    public static String getAppVersion(String packageName) throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = MyApplication.getInstance().getPackageManager().getPackageInfo(packageName, 0);
        String appVersion = packageInfo.versionName;
        return appVersion;
    }

    /**
     * 获取单个App的所有权限
     **/
    public static String[] getAppPermission(String packageName) throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = MyApplication.getInstance().getPackageManager().getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
        String[] permission = packageInfo.requestedPermissions;
        return permission;
    }

    /**
     * 获取单个App的签名
     **/
    public static String getAppSignature(String packageName) throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = MyApplication.getInstance().getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
        String allSignature = packageInfo.signatures[0].toCharsString();
        return allSignature;
    }

    /**
     * 获取version code
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context)//获取版本号(内部识别号)
    {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }
}
