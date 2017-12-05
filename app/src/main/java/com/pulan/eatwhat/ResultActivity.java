package com.pulan.eatwhat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pulan.entity.User;
import com.pulan.util.CommonUtil;
import com.pulan.util.DataPreference;
import com.pulan.widget.OverWatchLoadingView;

import java.text.ParseException;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by pulan on 17/11/22.
 * 计算结果页面
 */
public class ResultActivity extends BaseActivity {

    private static final String TAG = "ResultActivity";
    User user;
    @Bind(R.id.tv_bestEnergy)
    public TextView tv_bestEnergy;
    @Bind(R.id.ll_result)
    public LinearLayout ll_result;
    @Bind(R.id.loading_view)
    OverWatchLoadingView loadingView;
    @Bind(R.id.btn_iKnow)
    Button btn_iKnow;

    final static int MSG_STOP_ANIM = 0x1001;//停止加载动画
    final static int MSG_SAVE_SUCCESS = 0x1002;//保存云端成功
    final static int MSG_SAVE_FAILED = 0x1003;//保存云端失败
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SAVE_FAILED:
                    break;
                case MSG_SAVE_SUCCESS:
                    //关闭页面，跳至主页
                    start2Activity(MainActivity.class);
                    finish();
                    break;
                case MSG_STOP_ANIM:
                    ll_result.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initView();
    }

    private void initView() {
        //获取用户信息
        user = DataPreference.getUserInfo();
        if (!user.getPhoneNum().equals("")) {
            //根据公式计算
            boolean sex = user.getSex();
            int weight = Integer.valueOf(user.getWeight());
            int height = Integer.valueOf(user.getHeight());
            String birth = user.getBirth();
            //字符转日期
            Date birthDate = CommonUtil.parseStr2Date(birth);
            int age = CommonUtil.getAge(birthDate);
            int bestEnergy = caculateREE(sex, weight, height, age);
            user.setREE(bestEnergy);
            tv_bestEnergy.setText(bestEnergy + "");
        }


        //开启动画
        handler.sendEmptyMessageDelayed(MSG_STOP_ANIM, 2000);
    }

    @OnClick({R.id.btn_iKnow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_iKnow:
                //保存本地
                DataPreference.setUserInfo(user);
                //隐藏自己
                btn_iKnow.setVisibility(View.GONE);
                //开启动画
                loadingView.start();
                //保存云端
                user.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            handler.sendEmptyMessage(MSG_SAVE_SUCCESS);
                        } else {
                            Log.i(TAG, "请求失败：" + e.toString());
                            handler.sendEmptyMessage(MSG_SAVE_FAILED);
                        }
                    }
                });
                break;
        }
    }

    /**
     * 科学计算每天应该摄入多少卡路里
     *
     * @param sex
     * @param weight
     * @param height
     * @param age
     * @return
     */
    private int caculateREE(boolean sex, int weight, int height, int age) {
        int ree = 0;
        if (sex == true) {
            //女
            Double reeD = (10 * weight) + (6.25 * height) - (5 * age) - 161;
            ree = reeD.intValue();
        } else {
            //男
            Double reeD = (10 * weight) + (6.25 * height) - (5 * age) + 5;
            ree = reeD.intValue();
        }
        return ree;
    }
}
