package com.pulan.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pulan.eatwhat.R;
import com.pulan.entity.Eated;
import com.pulan.entity.User;
import com.pulan.util.DataPreference;
import com.pulan.util.PuLanUtil;

import java.util.List;

/**
 * Created by pupu on 2017/9/23.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private static final String TAG = "HistoryAdapter";
    List<HistoryModel> mDatas;
    Context mContext;
    LayoutInflater inflater;
    int REE;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                default:
                    break;
            }
        }
    };

    public HistoryAdapter(Context context, List<HistoryModel> mDatas) {
        this.mDatas = mDatas;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        User user = DataPreference.getUserInfo();
        this.REE = user.getREE();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_rv_history, null);
        if (parent.getChildCount() == 0) {
            parent.addView(view);
        }
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        HistoryModel model = mDatas.get(position);
        //设置时间
        holder.tv_time.setText(mDatas.get(position).getDateTime());
        //设置总热量
        List<Eated> breakfastFoods = model.getBreakfastFoods();
        List<Eated> lunchFoods = model.getLunchFoods();
        List<Eated> dinnerFoods = model.getDinnerFoods();
        List<Eated> snacksFoods = model.getSnackFoods();

        //当天吃过的食物总数
        int totalFoodSize = 0;

        int breakfastEnergy = 0;
        String breakfastFood = "";
        int breakSize = 1;
        for (int i = 0; breakfastFoods != null && i < breakfastFoods.size(); i++) {
            Eated eated = breakfastFoods.get(i);
            //显示早餐的总热量和详细数据
            breakfastEnergy += eated.getFood().getEnergy();
            breakfastFood = breakfastFood + eated.getFood().getName();
            if (i != breakfastFoods.size() - 1) {
                breakfastFood = breakfastFood + "\n";
            } else {
                totalFoodSize += (i + 1);
                breakSize = (i + 1);
            }
        }

        holder.tv_breakfastContent.setText(breakfastFood.equals("") ? "----" : breakfastFood);
        holder.tv_breakfastEnergy.setText(breakfastEnergy + "");

        int lunchEnergy = 0;
        String lunchFood = "";
        int lunchSize = 1;
        for (int i = 0; lunchFoods != null && i < lunchFoods.size(); i++) {
            Eated eated = lunchFoods.get(i);
            //显示早餐的总热量和详细数据
            lunchEnergy += eated.getFood().getEnergy();
            lunchFood = lunchFood + eated.getFood().getName();
            if (i != lunchFoods.size() - 1) {
                lunchFood = lunchFood + "\n";
            } else {
                totalFoodSize += (i + 1);
                lunchSize = (i + 1);
            }
        }

        holder.tv_lunchContent.setText(lunchFood.equals("") ? "----" : lunchFood);
        holder.tv_lunchEnergy.setText(lunchEnergy + "");

        int dinnerEnergy = 0;
        String dinnerFood = "";
        int dinnerSize = 1;
        for (int i = 0; dinnerFoods != null && i < dinnerFoods.size(); i++) {
            Eated eated = dinnerFoods.get(i);
            //显示早餐的总热量和详细数据
            dinnerEnergy += eated.getFood().getEnergy();
            dinnerFood = dinnerFood + eated.getFood().getName();
            if (i != dinnerFoods.size() - 1) {
                dinnerFood = dinnerFood + "\n";
            } else {
                totalFoodSize += (i + 1);
                dinnerSize = (i + 1);
            }
        }

        holder.tv_dinnerContent.setText(dinnerFood.equals("") ? "----" : dinnerFood);
        holder.tv_dinnerEnergy.setText(dinnerEnergy + "");

        int snackEnergy = 0;
        String snackFood = "";
        int snackSize = 1;
        for (int i = 0; snacksFoods != null && i < snacksFoods.size(); i++) {
            Eated eated = snacksFoods.get(i);
            //显示早餐的总热量和详细数据
            snackEnergy += eated.getFood().getEnergy();
            snackFood = snackFood + eated.getFood().getName();
            if (i != snacksFoods.size() - 1) {
                snackFood = snackFood + "\n";
            } else {
                totalFoodSize += (i + 1);
                snackSize = (i + 1);
            }
        }
        Log.i(TAG, "totalFoodSize===>" + totalFoodSize);
        int tmpSize = breakSize + lunchSize + dinnerSize + snackSize - 4;
        holder.tv_snackContent.setText(snackFood.equals("") ? "----" : snackFood);
        holder.tv_snackEnergy.setText(snackEnergy + "");

        //总能量
        int totalEnergy = breakfastEnergy + lunchEnergy + dinnerEnergy + snackEnergy;
        holder.tv_energy_total.setText(totalEnergy + "");

        //根据总能量判断今日摄入能量是否已经足够
        if (totalEnergy < REE) {
            //摄入不够
            holder.rl.setBackgroundResource(R.drawable.fade_energy_l);
        } else if (totalEnergy > REE * 1.75) {
            //摄入过多
            holder.rl.setBackgroundResource(R.drawable.fade_energy_h);
        } else {
            //正常摄入
            holder.rl.setBackgroundResource(R.drawable.fade_energy_m);
        }

        holder.rl_two.setTag(R.id.tmpSize, tmpSize);
        holder.rl_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                boolean flag = false;
                if (v.getTag() != null) {
                    flag = (boolean) v.getTag();
                }
                int tmpSize = (int) v.getTag(R.id.tmpSize);

                int height = PuLanUtil.dip2px(mContext, 116);
                int exHeight = height + PuLanUtil.dip2px(mContext, 96) + (tmpSize + 4) * PuLanUtil.sp2px(mContext, 16);
                Log.i(TAG, "tmpSize===>" + tmpSize);
                Log.i(TAG, "exHeight===>" + exHeight);

                if (flag == false) {
                    //展开动画
                    ValueAnimator animatorY = ValueAnimator.ofInt(height, exHeight);
                    animatorY.setDuration(300);
                    animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(final ValueAnimator animation) {
                            v.post(new Runnable() {
                                @Override
                                public void run() {
                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
                                    params.height = (int) animation.getAnimatedValue();
                                    v.setLayoutParams(params);
                                }
                            });
                        }
                    });
                    animatorY.start();

                    //旋转动画
                    ValueAnimator animatorR = ValueAnimator.ofFloat(0, 180);
                    animatorR.setDuration(300);
                    animatorR.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            holder.iv_arrow.setRotation((Float) animation.getAnimatedValue());
                        }
                    });
                    animatorR.start();
                    v.setTag(true);
                } else {
                    //收缩动画
                    ValueAnimator animatorY = ValueAnimator.ofInt(exHeight, height);
                    animatorY.setDuration(300);
                    animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(final ValueAnimator animation) {
                            v.post(new Runnable() {
                                @Override
                                public void run() {
                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
                                    params.height = (int) animation.getAnimatedValue();
                                    v.setLayoutParams(params);
                                }
                            });
                        }
                    });
                    animatorY.start();

                    //旋转动画
                    ValueAnimator animatorR = ValueAnimator.ofFloat(180, 0);
                    animatorR.setDuration(300);
                    animatorR.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            holder.iv_arrow.setRotation((Float) animation.getAnimatedValue());
                        }
                    });
                    animatorR.start();
                    v.setTag(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rl;
        RelativeLayout rl_one;
        RelativeLayout rl_two;
        ImageView iv_arrow;
        TextView tv_energy_total;
        LinearLayout ll_detail;
        TextView tv_time;
        TextView tv_breakfastContent;
        TextView tv_breakfastEnergy;
        TextView tv_lunchEnergy;
        TextView tv_lunchContent;
        TextView tv_dinnerContent;
        TextView tv_dinnerEnergy;
        TextView tv_snackContent;
        TextView tv_snackEnergy;

        public MyViewHolder(View itemView) {
            super(itemView);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            rl_one = (RelativeLayout) itemView.findViewById(R.id.rl_one);
            rl_two = (RelativeLayout) itemView.findViewById(R.id.rl_two);
            iv_arrow = (ImageView) itemView.findViewById(R.id.iv_arrow);
            tv_energy_total = (TextView) itemView.findViewById(R.id.tv_energy_total);
            ll_detail = (LinearLayout) itemView.findViewById(R.id.ll_detail);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_breakfastContent = (TextView) itemView.findViewById(R.id.tv_breakfastContent);
            tv_breakfastContent = (TextView) itemView.findViewById(R.id.tv_breakfastContent);
            tv_breakfastEnergy = (TextView) itemView.findViewById(R.id.tv_breakfastEnergy);
            tv_lunchContent = (TextView) itemView.findViewById(R.id.tv_lunchContent);
            tv_lunchEnergy = (TextView) itemView.findViewById(R.id.tv_lunchEnergy);
            tv_dinnerContent = (TextView) itemView.findViewById(R.id.tv_dinnerContent);
            tv_dinnerEnergy = (TextView) itemView.findViewById(R.id.tv_dinnerEnergy);
            tv_snackContent = (TextView) itemView.findViewById(R.id.tv_snackContent);
            tv_snackEnergy = (TextView) itemView.findViewById(R.id.tv_snackEnergy);
        }
    }
}
