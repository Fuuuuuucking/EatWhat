package com.pulan.util;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.pulan.adapter.MyFragmentAdapter;
import com.pulan.app.MyApplication;
import com.pulan.widget.CommonDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by pulan on 2017/8/18.
 */
public class CommonUtil {

    private static final String TAG = "CommonUtil";
    public static String sdRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    /**
     * 判断year是否为闰年
     *
     * @param year
     * @return true==>闰年;false==平年
     */
    public static boolean isLeapYear(int year) {
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            //是闰年
            return true;
        }
        return false;
    }

    /**
     * 强制弹出软键盘
     *
     * @param view
     */
    public static void showSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) MyApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) MyApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
        if (isOpen == true) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 获取android系统版本号
     */
    public static int getApiVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 显示提示
     */
    public static void showToast(String msg, boolean timeLong) {
        ToastUtils.getInstance().showToast(msg, timeLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
    }

    /**
     * 显示提示
     */
    public static void showToast(String msg) {
        ToastUtils.getInstance().showToast(msg, Toast.LENGTH_SHORT);
    }

    /**
     * 显示提示
     */
    public static void showToast(int resId, boolean timeLong) {
        showToast(getString(resId), timeLong);
    }

    /**
     * 显示提示
     */
    public static void showToast(int resId) {
        showToast(getString(resId), false);
    }

    public static String getString(int resId) {
        return MyApplication.getInstance().getString(resId);
    }

    /**
     * 判断字符串是否为电话号码
     *
     * @param str 待验证的字符串
     * @return 如果是符合邮箱格式的字符串, 返回true, 否则为false
     */
    public static boolean isPhoneNum(String str) {
        String regex = "^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";
        return Pattern.matches(regex, str);
    }

    /**
     * 计算渐变颜色中间色值
     *
     * @param startColor 起始颜色
     * @param endColor   结束颜色
     * @param radio      百分比，取值范围【0~1】
     * @return 颜色值
     */
    public static int getColor(int startColor, int endColor, float radio) {
        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);

        int red = (int) (redStart + ((redEnd - redStart) * radio + 0.5));
        int greed = (int) (greenStart + ((greenEnd - greenStart) * radio + 0.5));
        int blue = (int) (blueStart + ((blueEnd - blueStart) * radio + 0.5));
        return Color.argb(255, red, greed, blue);
    }

    /**
     * 字符转日期date类型
     *
     * @param strDate
     * @return
     * @throws ParseException
     */
    public static Date parseStr2Date(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据出生日期计算年龄
     *
     * @param birthDay
     * @return
     * @throws Exception
     */
    public static int getAge(Date birthDay) {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            Log.i(TAG, "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                age--;
            }
        }
        return age;
    }

    /**
     * 显示通用对话框
     *
     * @param content  对话框内容
     * @param okText   确定按钮文字
     * @param listener 按钮点击
     * @return
     */
    public static CommonDialog showCommonDialog(Context context, String content, String okText, DialogInterface.OnClickListener listener) {
        CommonDialog.Builder builder = new CommonDialog.Builder(context);
        CommonDialog dialog = builder.setContent(content)
                .setOk(okText, listener)
                .create();
        dialog.show();
        return dialog;
    }

    /**
     * 检查当前网络是否可用
     *
     * @return true==>可用;false==>不可用
     */

    public static boolean isNetworkAvailable() {
        Context context = MyApplication.getInstance().getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
