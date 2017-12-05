package com.pulan.eatwhat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pulan.adapter.CommonPagerAdapter;
import com.pulan.fragment.ChartFragment;
import com.pulan.fragment.HistoryFragment;
import com.pulan.fragment.MainFragment;
import com.tencent.bugly.crashreport.CrashReport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseActivity {

    private static String TAG = "MainActivity";

    //Container
    @Bind(R.id.rl)
    public RelativeLayout rl;

    //view pager
    @Bind(R.id.vp_main)
    public ViewPager vp_mian;
    CommonPagerAdapter adapter;
    List<Fragment> fragments;

    boolean isExit = false;

    public static MainActivity instance;

    public static MainActivity getInstance() {
        return instance;
    }

    public ViewPager getVpMain() {
        return vp_mian;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                default:
                    isExit = false;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
//        CrashReport.testJavaCrash();

        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        //init pager
        fragments = new ArrayList<>();
        fragments.add(new HistoryFragment());
        fragments.add(new MainFragment());
        fragments.add(new ChartFragment());
        adapter = new CommonPagerAdapter(getSupportFragmentManager(), fragments);
        vp_mian.setAdapter(adapter);
        //show second fragment:mainfragment
        vp_mian.setCurrentItem(1);

        //set background color
        //获取时间,采取24小时制
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String hour = sdf.format(new Date());
        int tmpHour = Integer.parseInt(hour);
        Log.i("tmpHour==>", tmpHour + "");
        if (tmpHour >= 6 && tmpHour <= 8) {
            //breakfast
            rl.setBackgroundColor(getResources().getColor(R.color.white));
        } else if (tmpHour >= 11 && tmpHour <= 14) {
            //lunch
            rl.setBackgroundColor(getResources().getColor(R.color.pulanBlue));
        } else if (tmpHour >= 17 && tmpHour <= 19) {
            //dinner
            rl.setBackgroundColor(getResources().getColor(R.color.pulanGray));
        } else {
            //snacks
            rl.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 点击两次退出程序
     */
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            //参数用作状态码；根据惯例，非 0 的状态码表示异常终止。
            System.exit(0);
        }
    }
}
