package com.pulan.eatwhat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.barlibrary.ImmersionBar;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;

/**
 * Created by pupu on 2017/9/22.
 */
public class BaseActivity extends AppCompatActivity {

    private ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true).init();
        //获取时间,采取24小时制
//        SimpleDateFormat sdf = new SimpleDateFormat("HH");
//        String hour = sdf.format(new Date());
//        int tmpHour = Integer.parseInt(hour);
//        Log.i("tmpHour==>", tmpHour + "");
//        if (tmpHour >= 6 && tmpHour <= 8) {
//            mImmersionBar.statusBarDarkFont(true).init();
//        } else if (tmpHour >= 11 && tmpHour <= 14) {
//            mImmersionBar.statusBarDarkFont(false).init();
//        } else if (tmpHour >= 17 && tmpHour <= 19) {
//            mImmersionBar.statusBarDarkFont(false).init();
//        } else {
//            mImmersionBar.statusBarDarkFont(true).init();
//        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
    }

    /**
     * 开启新页面
     *
     * @param clazz
     */
    public void start2Activity(Class clazz) {
        Intent it = new Intent(this, clazz);
        startActivity(it);
    }

    /**
     * 开启新页面，带参数
     *
     * @param clazz
     */
    public void start2Activity(Class clazz, Bundle bundle) {
        Intent it = new Intent(this, clazz);
        it.putExtras(bundle);
        startActivity(it);
    }
}
