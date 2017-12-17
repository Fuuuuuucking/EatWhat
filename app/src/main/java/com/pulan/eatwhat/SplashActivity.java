package com.pulan.eatwhat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pulan.entity.Food;
import com.pulan.entity.PhoneInfo;
import com.pulan.entity.User;
import com.pulan.util.DataPreference;
import com.pulan.util.Logger;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by pulan on 2017/9/29.
 */

public class SplashActivity extends BaseActivity {

    final static String TAG = "SplashActivity";

    final static int GO_MAIN = 0x0001;//跳转至主页
    final static int GO_DATA = 0x0002;//跳转数据采集
    int goAfter;

    List<Food> foodList;

    final static int MSG_JUMP = 0x1001;//跳转Main
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_JUMP:
                    if (goAfter == GO_DATA) {
                        start2Activity(RegisterActivity.class);
                        finish();
                    } else if (goAfter == GO_MAIN) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //是否为第一次进入app
        PhoneInfo info = DataPreference.getPhoneInfo();
        User user = DataPreference.getUserInfo();
        if (info.isFisrtOpen() && (user.getPhoneNum() == null || user.getPhoneNum().equals(""))) {
            //本地存储
            info.setFisrtOpen(false);
            DataPreference.setPhoneInfo(info);
            //跳转至采集信息页面
            goAfter = GO_DATA;
        } else {
            //跳转至主页
            goAfter = GO_MAIN;
        }

        //获取本地食物列表
        foodList = DataPreference.getAllFoods();
        getCloudFood();
    }

    /**
     * 获取云上的所有食物
     */
    private void getCloudFood() {
        if (foodList == null) {
            BmobQuery<Food> query = new BmobQuery<>();
            query.findObjects(new FindListener<Food>() {
                @Override
                public void done(List<Food> list, BmobException e) {
                    if (e == null) {
                        Log.i(TAG, list.toString());
                        //存储本地
                        if (foodList == null || foodList.size() == 0) {
                            DataPreference.setAllFoods(list);
                        } else {
                            //比较
                            if (list.size() != foodList.size()) {
                                DataPreference.setAllFoods(list);
                            }
                        }
                        handler.sendEmptyMessageDelayed(MSG_JUMP, 1000);
                    } else {
                        Log.e(TAG, e.toString());
                    }
                }
            });
        } else {
            handler.sendEmptyMessageDelayed(MSG_JUMP, 1000);
        }

    }
}
