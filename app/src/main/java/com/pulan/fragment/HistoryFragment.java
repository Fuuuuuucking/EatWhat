package com.pulan.fragment;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pulan.adapter.HistoryAdapter;
import com.pulan.adapter.HistoryModel;
import com.pulan.eatwhat.MainActivity;
import com.pulan.eatwhat.R;
import com.pulan.entity.Eated;
import com.pulan.util.DataPreference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by pupu on 2017/9/22.
 */

public class HistoryFragment extends BaseFragment {

    final static String TAG = "HistoryFragment";

    //expandable list view
    @Bind(R.id.rv_history)
    public RecyclerView rv_history;
    HistoryAdapter adapter;
    List<HistoryModel> mDatas;
    List<Eated> list_eated;

    @Bind(R.id.tv_title)
    public TextView tv_title;
    @Bind(R.id.iv_index)
    ImageView iv_index;

    @OnClick({R.id.iv_index})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_index:
                MainActivity.getInstance().getVpMain().setCurrentItem(1);
                break;
        }
    }

    /**
     * 更新吃过列表的数据
     */
    private void refreshRvData() {
        Calendar cal = Calendar.getInstance();
        int curYear = cal.get(Calendar.YEAR);
        int curMonth = cal.get(Calendar.MONTH) + 1;
        int curDay = cal.get(Calendar.DAY_OF_MONTH);
        if (mDatas.size() != 0) {
            mDatas.clear();
        }
        //取本地吃过的历史记录并刷新列表
        list_eated = DataPreference.getAllEated();
        if (list_eated != null) {
            //处理数据
            HistoryModel model = new HistoryModel();
            List<Eated> breakfastFoods = new ArrayList<>();
            List<Eated> lunchFoods = new ArrayList<>();
            List<Eated> dinnerFoods = new ArrayList<>();
            List<Eated> snackFoods = new ArrayList<>();
            for (int i = 0; i < list_eated.size(); i++) {
                boolean isNeedAdd = false;
                Eated eated = list_eated.get(i);
                int year = Integer.parseInt(eated.getTime().substring(0, 4));
                int month = Integer.parseInt(eated.getTime().substring(5, 7));
                int day = Integer.parseInt(eated.getTime().substring(8, 10));

                if (i > 0) {
                    Eated tmpEated = list_eated.get(i - 1);
                    //判断相邻两个食物是否是同一天
                    int tmpYear = Integer.parseInt(tmpEated.getTime().substring(0, 4));
                    int tmpMonth = Integer.parseInt(tmpEated.getTime().substring(5, 7));
                    int tmpDay = Integer.parseInt(tmpEated.getTime().substring(8, 10));
                    if (tmpYear == year && tmpMonth == month && tmpDay == day) {
                        //如果是同一天,添加至同一model
                        isNeedAdd = false;
                    } else {
                        isNeedAdd = true;
                        //如果不是同一天new Model
                        model = new HistoryModel();
                        lunchFoods = new ArrayList<>();
                        dinnerFoods = new ArrayList<>();
                        snackFoods = new ArrayList<>();
                    }
                }

                if (curYear == year && curMonth == month && curDay == day) {
                    //今天
                    model.setDateTime("今天");
                } else if (curYear == year && curMonth == month && curDay == (day + 1)) {
                    //昨天
                    model.setDateTime("昨天");
                } else if (curYear == year && curMonth == month && curDay == (day + 2)) {
                    //前天
                    model.setDateTime("前天");
                } else {
                    //写日期
                    model.setDateTime(month + "月" + day + "日");
                }

                if (eated.getEatTime().equals("breakfast")) {
                    //早餐
                    breakfastFoods.add(eated);
                } else if (eated.getEatTime().equals("lunch")) {
                    lunchFoods.add(eated);
                } else if (eated.getEatTime().equals("dinner")) {
                    dinnerFoods.add(eated);
                } else if (eated.getEatTime().equals("snacks")) {
                    snackFoods.add(eated);
                }
                model.setBreakfastFoods(breakfastFoods);
                model.setLunchFoods(lunchFoods);
                model.setDinnerFoods(dinnerFoods);
                model.setSnackFoods(snackFoods);

                if (i == 0 || isNeedAdd == true) {
                    mDatas.add(model);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        refreshRvData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            refreshRvData();
            if (tv_title != null) {
                //set tv_title translate
                ValueAnimator animatorA = ValueAnimator.ofFloat(0f, 1f);
                animatorA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        tv_title.setAlpha((float) animation.getAnimatedValue());
                    }
                });
                ValueAnimator animatorX = ValueAnimator.ofFloat(-100f, 0f);
                animatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        tv_title.setTranslationX((float) animation.getAnimatedValue());
                    }
                });
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animatorX, animatorA);
                animatorSet.setDuration(300);
                animatorSet.start();
            }
        } else {
            if (tv_title != null) {
                tv_title.setTranslationX(-100f);
                tv_title.setAlpha(0f);
            }
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_history;
    }

    @Override
    protected void initAllViewMembers(Bundle savedInstanceState) {
        tv_title.setTranslationX(-100f);
        tv_title.setAlpha(0f);
        changeUI();

        //init recyclerview
        mDatas = new ArrayList<>();
        adapter = new HistoryAdapter(context, mDatas);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        rv_history.setLayoutManager(manager);
        rv_history.setAdapter(adapter);
    }

    private void changeUI() {
        //获取时间,采取24小时制
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String hour = sdf.format(new Date());
        int tmpHour = Integer.parseInt(hour);
        Log.i("tmpHour==>", tmpHour + "");
        if (tmpHour >= 6 && tmpHour <= 8) {
            //breakfast
            tv_title.setTextColor(getResources().getColor(R.color.pulanBlack));
            iv_index.setImageResource(R.drawable.menu_half);
        } else if (tmpHour >= 11 && tmpHour <= 14) {
            //lunch
            tv_title.setTextColor(getResources().getColor(R.color.white));
            iv_index.setImageResource(R.drawable.menu_half_w);
        } else if (tmpHour >= 17 && tmpHour <= 19) {
            //dinner
            tv_title.setTextColor(getResources().getColor(R.color.white));
            iv_index.setImageResource(R.drawable.menu_half_w);
        } else {
            //snacks
            tv_title.setTextColor(getResources().getColor(R.color.pulanBlack));
            iv_index.setImageResource(R.drawable.menu_half);
        }
    }
}
