package com.pulan.app;

import android.app.Application;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

import cn.bmob.v3.Bmob;

/**
 * Created by pupu on 2017/9/22.
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    static MyApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        //init Bugly
        Bugly.init(getApplicationContext(), "8d9051b815", false);

        //异常捕获初始化
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());

        // 初始化BmobSDK
        Bmob.initialize(this, "8dd26f729626ec7ce2961cf66bc53e65");
    }


    public static MyApplication getInstance() {
        if (mContext == null) {
            mContext = new MyApplication();
        }
        return mContext;
    }


}
