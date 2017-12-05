package com.pulan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pulan.eatwhat.R;
import com.pulan.eatwhat.RegisterActivity;
import com.pulan.eatwhat.ResultActivity;
import com.pulan.entity.User;
import com.pulan.util.DataPreference;
import com.pulan.widget.NoScrollViewPager;
import com.pulan.widget.OverWatchLoadingView;
import com.pulan.widget.RulerView;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by pulan on 17/11/22.
 */

public class WeightFragment extends BaseFragment {

    private static final String TAG = "WeightFragment";
    @Bind(R.id.ruler_weight)
    RulerView ruler_weight;
    @Bind(R.id.tv_indicator)
    TextView tv_indicator;
    String weight;
    @Bind(R.id.loading_view)
    OverWatchLoadingView loadingView;

    final static int MSG_CHANGE_WEIGHT = 0x1001;//改变吨位事件
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_CHANGE_WEIGHT:
                    //改变文字
                    int value = (int) ((float) msg.obj / 1);
                    tv_indicator.setText(value + "");
                    weight = String.valueOf(value);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public int getContentViewId() {
        return R.layout.fragment_weight;
    }

    @Override
    protected void initAllViewMembers(Bundle savedInstanceState) {
        ruler_weight.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                Log.i(TAG, "ruler_weight value===>" + value);
                Message msg = new Message();
                msg.what = MSG_CHANGE_WEIGHT;
                msg.obj = value;
                handler.sendMessage(msg);
            }
        });
        ruler_weight.setValue(60, 35, 120, 1);
        ruler_weight.setStartColor(0xff06f967);
        ruler_weight.setEndColor(0xffffa200);
        weight = "60";
    }

    @OnClick({R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                //本地存储
                User user = DataPreference.getUserInfo();
                user.setWeight(weight);
                DataPreference.setUserInfo(user);
                //开启动画
                loadingView.start();
                //跳转下一页
                if (RegisterActivity.getInstance() != null) {
                    NoScrollViewPager vp = RegisterActivity.getInstance().getVpRegister();
                    int childCount = vp.getAdapter().getCount();
                    int curIndex = vp.getCurrentItem();
                    if (curIndex < childCount - 1) {
                        vp.setCurrentItem(curIndex + 1);
                    } else {
                        //跳转到计算结果页面
                        Intent it = new Intent(getActivity(), ResultActivity.class);
                        startActivity(it);
                        getActivity().finish();
                    }
                }
                break;
        }
    }
}
