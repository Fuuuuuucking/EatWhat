package com.pulan.fragment;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pulan.eatwhat.MainActivity;
import com.pulan.eatwhat.R;
import com.pulan.eatwhat.SettingActivity;
import com.pulan.entity.Eated;
import com.pulan.entity.Food;
import com.pulan.util.DataPreference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by pupu on 2017/9/22.
 */

public class MainFragment extends BaseFragment {

    final static String TAG = "MainFragment";

    //face
    @Bind(R.id.iv_face)
    public ImageView iv_face;
    //circle
    @Bind(R.id.iv_c1)
    public ImageView iv_c1;
    @Bind(R.id.iv_c2)
    public ImageView iv_c2;
    @Bind(R.id.iv_c3)
    public ImageView iv_c3;
    //animator Set
    private AnimatorSet set_c1;
    private AnimatorSet set_c2;
    private AnimatorSet set_c3;
    private AnimatorSet set_face;
    private static int DELAY_START = 0;//默认延迟1s播放
    //textView
    @Bind(R.id.tv_result)
    public TextView tv_result;
    @Bind(R.id.tv_gift)
    public TextView tv_gift;
    @Bind(R.id.tv_center)
    TextView tv_center;
    @Bind(R.id.iv_index_right)
    ImageView iv_index_right;
    @Bind(R.id.iv_index)
    ImageView iv_index;
    @Bind(R.id.iv_setting)
    ImageView iv_setting;

    //一日三餐中的哪一餐的FLAG,与数据库的字段名称对应
    private static String BREAKFAST = "breakfast";
    private static String LUNCH = "lunch";
    private static String DINNER = "dinner";
    private static String SNACKS = "snacks";
    private String curTime = SNACKS;

    private static boolean chooseFlag = false;//是否已选择标志

    //food：食物列表
    private List<Food> list_food;
    private int index = 0;
    private Timer timer = new Timer();
    private static int FLAG_TIMER_START = 1;//定时器启动标志
    private static int FLAG_TIMER_STOP = 2;//定时器停止标志
    private int flag = FLAG_TIMER_STOP;//定时器启动、停止标志
    //注意要在主线程上绘图
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x01) {
                if (index < list_food.size()) {
                    tv_result.setText(list_food.get(index).getName() +
                            " (" +
                            list_food.get(index).getEnergy() + " cal"
                            + ")");
                }
            }
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        } else {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁掉相关数据
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        handler = null;
        list_food = null;
        chooseFlag = false;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initAllViewMembers(Bundle savedInstanceState) {
        setCurTime();
        changeUI();

        //获取本地食物列表
        if (DataPreference.getAllFoods() != null && DataPreference.getAllFoods().size() != 0) {
            if (curTime.equals(BREAKFAST)) {
                list_food = DataPreference.getFoodBreakfast();
            } else if (curTime.equals(LUNCH)) {
                list_food = DataPreference.getFoodLunch();
            } else if (curTime.equals(DINNER)) {
                list_food = DataPreference.getFoodDinner();
            } else {
                list_food = DataPreference.getFoodSnack();
            }
        }
        //animator set
        set_c1 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.animator_c1);
        set_c1.setTarget(iv_c1);
        set_c1.setStartDelay(DELAY_START);
        set_c1.start();

        set_c2 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.animator_c2);
        set_c2.setTarget(iv_c2);
        set_c2.setStartDelay(DELAY_START);
        set_c2.start();

        set_c3 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.animator_c3);
        set_c3.setTarget(iv_c3);
        set_c3.setStartDelay(DELAY_START);
        set_c3.start();

        set_face = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.animator_face);
        set_face.setTarget(iv_face);
        set_face.setStartDelay(DELAY_START);
        set_face.start();
    }

    /**
     * 设置稍后吃什么的结果
     */
    private void randomFood() {
        //设置定时器
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 0x01;
                handler.sendMessage(msg);
                index++;
                if (index >= list_food.size()) {
                    index = 0;
                }
            }
        }, 0, 100);
    }

    @OnClick({R.id.iv_c1, R.id.iv_index, R.id.iv_index_right, R.id.iv_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
                Intent it = new Intent();
                it.setClass(getActivity(), SettingActivity.class);
                startActivity(it);
                getActivity().overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                break;
            case R.id.iv_index:
                MainActivity.getInstance().getVpMain().setCurrentItem(0);
                break;
            case R.id.iv_index_right:
                MainActivity.getInstance().getVpMain().setCurrentItem(2);
                break;
            case R.id.iv_c1:
                if (flag == FLAG_TIMER_START) {
                    timer.cancel();
                    timer = null;
                    flag = FLAG_TIMER_STOP;
                    //改变动画
                    set_c1.start();

                    set_c2 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.animator_c2);
                    set_c2.setTarget(iv_c2);
                    set_c2.setStartDelay(0);
                    set_c2.start();

                    set_c3 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.animator_c3);
                    set_c3.setTarget(iv_c3);
                    set_c3.setStartDelay(0);
                    set_c3.start();

                    set_face.start();
                } else {
                    flag = FLAG_TIMER_START;
                    timer = new Timer();
                    randomFood();
                    //改变动画
                    set_c1.end();
                    set_face.end();

                    set_c2 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.animator_c2_reverse);
                    set_c2.setTarget(iv_c2);
                    set_c2.setStartDelay(0);
                    set_c2.start();

                    set_c3 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.animator_c3_reverse);
                    set_c3.setTarget(iv_c3);
                    set_c3.setStartDelay(0);
                    set_c3.start();
                }
                if (chooseFlag == false) {
                    chooseFlag = true;
                } else {
                    iv_c1.setEnabled(false);
                    //存储吃过的历史记录
                    Eated eated = new Eated();
                    if (index >= list_food.size()) {
                        index = list_food.size() - 1;
                    }
                    Food tmpFood = list_food.get(index);
                    eated.setFood(tmpFood);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
                    String nowTime = format.format(new Date());
                    eated.setTime(nowTime);
                    eated.setEatTime(curTime);
                    DataPreference.addEated(eated);
                }
                break;
        }
    }

    /**
     * 设置当前用餐情况
     */
    private void setCurTime() {
        //获取时间,采取24小时制
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String hour = sdf.format(new Date());
        int tmpHour = Integer.parseInt(hour);
        Log.i("tmpHour==>", tmpHour + "");
        if (tmpHour >= 6 && tmpHour <= 8) {
            curTime = BREAKFAST;
        } else if (tmpHour >= 11 && tmpHour <= 14) {
            curTime = LUNCH;
        } else if (tmpHour >= 17 && tmpHour <= 19) {
            curTime = DINNER;
        } else {
            curTime = SNACKS;
        }
        Log.i("curTime==>", curTime + "");
    }

    /**
     * 根据当前时间判断稍后用餐及改变UI
     */
    private void changeUI() {
        String typeFormat = getResources().getString(R.string.eat_time_type);
        //早餐/中餐/晚餐/零食
        if (curTime == BREAKFAST) {
            tv_center.setText(String.format(typeFormat, "早餐"));
            tv_center.setTextColor(getResources().getColor(R.color.pulanBlack));
            //变颜色
            tv_result.setTextColor(getResources().getColor(R.color.pulanBlue));
            tv_gift.setTextColor(getResources().getColor(R.color.pulanBlack));

            iv_c1.setColorFilter(getResources().getColor(R.color.pulanOrange));
            iv_c2.setColorFilter(getResources().getColor(R.color.pulanOrange));
            iv_c3.setColorFilter(getResources().getColor(R.color.pulanOrange));

            iv_index.setImageResource(R.drawable.menu_half);
            iv_index_right.setImageResource(R.drawable.chart_half_left);
            iv_setting.setImageResource(R.drawable.img_setting);
        } else if (curTime == LUNCH) {
            tv_center.setText(String.format(typeFormat, "午餐"));
            tv_center.setTextColor(getResources().getColor(R.color.white));
            //变颜色
            tv_result.setTextColor(getResources().getColor(R.color.pulanYellow));
            tv_gift.setTextColor(getResources().getColor(R.color.white));

            iv_c1.setColorFilter(getResources().getColor(R.color.pulanYellow));
            iv_c2.setColorFilter(getResources().getColor(R.color.pulanYellow));
            iv_c3.setColorFilter(getResources().getColor(R.color.pulanYellow));

            iv_index.setImageResource(R.drawable.menu_half_w);
            iv_index_right.setImageResource(R.drawable.chart_half_left_w);
            iv_setting.setImageResource(R.drawable.img_setting_w);
        } else if (curTime == DINNER) {
            tv_center.setText(String.format(typeFormat, "晚餐"));
            tv_center.setTextColor(getResources().getColor(R.color.white));
            //变颜色
            tv_result.setTextColor(getResources().getColor(R.color.pulanRed));
            tv_gift.setTextColor(getResources().getColor(R.color.white));

            iv_c1.setColorFilter(getResources().getColor(R.color.pulanRed));
            iv_c2.setColorFilter(getResources().getColor(R.color.pulanRed));
            iv_c3.setColorFilter(getResources().getColor(R.color.pulanRed));

            iv_index.setImageResource(R.drawable.menu_half_w);
            iv_index_right.setImageResource(R.drawable.chart_half_left_w);
            iv_setting.setImageResource(R.drawable.img_setting_w);
        } else {
            tv_center.setText(String.format(typeFormat, "零食"));
            tv_center.setTextColor(getResources().getColor(R.color.pulanBlack));
            //变颜色
            tv_result.setTextColor(getResources().getColor(R.color.pulanBlue));
            tv_gift.setTextColor(getResources().getColor(R.color.pulanBlack));

            iv_c1.setColorFilter(getResources().getColor(R.color.pulanRed));
            iv_c2.setColorFilter(getResources().getColor(R.color.pulanBlue));
            iv_c3.setColorFilter(getResources().getColor(R.color.pulanGreen));

            iv_index.setImageResource(R.drawable.menu_half);
            iv_index_right.setImageResource(R.drawable.chart_half_left);
            iv_setting.setImageResource(R.drawable.img_setting);
        }
    }
}
