package com.pulan.eatwhat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pulan.entity.User;
import com.pulan.util.CommonUtil;
import com.pulan.util.DataPreference;
import com.pulan.widget.LoadingButton;
import com.pulan.widget.RulerView;

import butterknife.Bind;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by pulan on 17/12/1.
 */

public class WeightActivity extends BaseActivity {

    private static final String TAG = "WeightActivity";

    @Bind(R.id.ruler_weight)
    RulerView ruler_weight;
    @Bind(R.id.btn_done)
    LoadingButton btn_done;
    @Bind(R.id.tv_indicator)
    TextView tv_indicator;
    User user;
    String weight;

    final static int MSG_CHANGE_WEIGHT = 0x1001;
    final static int MSG_SAVE_SUCCESS = 0x1002;
    final static int MSG_SAVE_FAILED = 0x1003;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SAVE_SUCCESS:
                    btn_done.stopAnimation();
                    //提示设置成功
                    CommonUtil.showToast(R.string.toast_setSuccess);
                    finish();
                    break;
                case MSG_SAVE_FAILED:
                    btn_done.stopAnimation();
                    //提示设置成功
                    CommonUtil.showToast(R.string.toast_setFailed);
                    finish();
                    break;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        initData();
        initView();
    }

    private void initData() {
        user = DataPreference.getUserInfo();
    }

    private void initView() {
        tv_indicator.setText(user.getWeight());

        final float weightF = Float.parseFloat(user.getWeight());
        ruler_weight.setValue(weightF, 35, 120, 1);
        ruler_weight.setStartColor(0xff06f967);
        ruler_weight.setEndColor(0xffffa200);
        ruler_weight.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                Log.i(TAG, "ruler_height value===>" + value);
                Message msg = new Message();
                msg.what = MSG_CHANGE_WEIGHT;
                msg.obj = value;
                handler.sendMessage(msg);
            }
        });

        //OnClick
        btn_done.setClickListener(new LoadingButton.ClickListener() {
            @Override
            public void onClick(View view) {
                //保存
                user.setWeight(weight);
                DataPreference.setUserInfo(user);
                btn_done.setEnabled(false);
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
            }
        });
    }


}
