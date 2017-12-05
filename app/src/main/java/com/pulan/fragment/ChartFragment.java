package com.pulan.fragment;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.pulan.adapter.HistoryModel;
import com.pulan.eatwhat.MainActivity;
import com.pulan.eatwhat.R;
import com.pulan.entity.Eated;
import com.pulan.util.DataPreference;
import com.pulan.widget.MyMarkerView;

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

public class ChartFragment extends BaseFragment {

    final static float MIN_ENERGY = 1600f;

    @Bind(R.id.lc_energy)
    LineChart lc_energy;
    @Bind(R.id.iv_index_left)
    ImageView iv_index_left;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.tv_unit)
    TextView tv_uint;
    @Bind(R.id.cv_qrcode)
    CardView cv_qrcode;

    @Bind(R.id.rl_qrcode)
    RelativeLayout rl_qrcode;

    List<HistoryModel> mDatas;
    List<Eated> list_eated;

    ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
    LineData chartData;
    ArrayList<Entry> values = new ArrayList<Entry>();

    @OnClick({R.id.cv_qrcode, R.id.iv_index_left, R.id.rl_qrcode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cv_qrcode:
                //show qrcode dialog
                rl_qrcode.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_index_left:
                MainActivity.getInstance().getVpMain().setCurrentItem(1);
                break;
            case R.id.rl_qrcode:
                rl_qrcode.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_chart;
    }

    @Override
    protected void initAllViewMembers(Bundle savedInstanceState) {
        tv_title.setTranslationX(100f);
        tv_title.setAlpha(0f);

        //init chart
        initChart();

        //according time change UI
        changeUI();
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
            tv_uint.setTextColor(getResources().getColor(R.color.pulanBlack));
            iv_index_left.setImageResource(R.drawable.chart_half_right);
        } else if (tmpHour >= 11 && tmpHour <= 14) {
            //lunch
            tv_title.setTextColor(getResources().getColor(R.color.white));
            tv_uint.setTextColor(getResources().getColor(R.color.white));
            iv_index_left.setImageResource(R.drawable.chart_half_right_w);
        } else if (tmpHour >= 17 && tmpHour <= 19) {
            //dinner
            tv_title.setTextColor(getResources().getColor(R.color.white));
            tv_uint.setTextColor(getResources().getColor(R.color.white));
            iv_index_left.setImageResource(R.drawable.chart_half_right_w);
        } else {
            //snacks
            tv_title.setTextColor(getResources().getColor(R.color.pulanBlack));
            tv_uint.setTextColor(getResources().getColor(R.color.pulanBlack));
            iv_index_left.setImageResource(R.drawable.chart_half_right);
        }
    }

    private void refreshChartData() {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
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
            }
        }
        //根据mDatas刷新列表数据
        if (mDatas.size() < 3) {
            //数据太少
            lc_energy.setData(null);
        } else {
            if (values.size() != 0) {
                values.clear();
            }
            for (int i = 0; i < mDatas.size(); i++) {
                //不算今天 && 不算4个以上的数据
                if (i == 4) {
                    break;
                }
                HistoryModel model = null;
                if (mDatas.size() < 4) {
//                        if ((mDatas.size() - i - 1) != 0) {
                    model = mDatas.get(mDatas.size() - i - 1);
                } else {
                    model = mDatas.get(3 - i);
                }
                int totalEnergy = 0;
                if (model != null) {
                    List<Eated> breakFoods = model.getBreakfastFoods();
                    List<Eated> lunchFoods = model.getLunchFoods();
                    List<Eated> dinnerFoods = model.getDinnerFoods();
                    List<Eated> snackFoods = model.getSnackFoods();
                    if (breakFoods != null) {
                        for (Eated eated : breakFoods) {
                            totalEnergy += eated.getFood().getEnergy();
                        }
                    }
                    if (lunchFoods != null) {
                        for (Eated eated : lunchFoods) {
                            totalEnergy += eated.getFood().getEnergy();
                        }
                    }
                    if (dinnerFoods != null) {
                        for (Eated eated : dinnerFoods) {
                            totalEnergy += eated.getFood().getEnergy();
                        }
                    }
                    if (snackFoods != null) {
                        for (Eated eated : snackFoods) {
                            totalEnergy += eated.getFood().getEnergy();
                        }
                    }
                    values.add(new Entry(i, totalEnergy, null));
                }
            }
        }
        lc_energy.notifyDataSetChanged();
        lc_energy.invalidate();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            refreshChartData();
            if (lc_energy != null) {
                lc_energy.animateXY(800, 800);
            }
            if (tv_title != null) {
                //set tv_title translate
                ValueAnimator animatorA = ValueAnimator.ofFloat(0f, 1f);
                animatorA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        tv_title.setAlpha((float) animation.getAnimatedValue());
                    }
                });
                ValueAnimator animatorX = ValueAnimator.ofFloat(100f, 0f);
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
                tv_title.setTranslationX(100f);
                tv_title.setAlpha(0f);
            }
        }
    }

    private void initChart() {

        lc_energy.setDrawGridBackground(false);
        lc_energy.getDescription().setEnabled(false);
        lc_energy.setTouchEnabled(true);
        lc_energy.setDragEnabled(true);
        lc_energy.setScaleEnabled(true);
        lc_energy.setPinchZoom(true);
        lc_energy.animateXY(800, 800);

        MyMarkerView mv = new MyMarkerView(context, R.layout.custom_marker_view);
        mv.setChartView(lc_energy); // For bounds control
        lc_energy.setMarker(mv); // Set the marker to the chart

        XAxis xAxis = lc_energy.getXAxis();
        xAxis.enableGridDashedLine(0f, 0f, 0f);
        xAxis.setEnabled(false);

        YAxis yAxis = lc_energy.getAxisRight();
        yAxis.setEnabled(false);

        Legend l = lc_energy.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setEnabled(true);

        if (values != null && values.size() > 0) {

        } else {
            refreshChartData();
        }

        LineDataSet set1;
        set1 = new LineDataSet(values, "热量");

        set1.setDrawIcons(false);
        set1.setColor(Color.parseColor("#fff4e34f"));
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1.5f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);
        set1.setFormLineWidth(0f);
        set1.setFormSize(15.f);
        //set cubic
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        //set not point
        set1.setDrawCircles(false);
        //set no values
        set1.setDrawValues(false);
        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.fade_red);
            set1.setFillDrawable(drawable);
        }

        if (dataSets.size() == 0) {
            dataSets.add(set1); // add the datasets
        }

        // create a data object with the datasets
        chartData = new LineData(dataSets);
        // set data
        lc_energy.setData(chartData);
        lc_energy.notifyDataSetChanged();
        lc_energy.invalidate();
    }
}
