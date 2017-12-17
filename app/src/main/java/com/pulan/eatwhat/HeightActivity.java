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
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by pulan on 17/11/30.
 */

public class HeightActivity extends BaseActivity {

    private static final String TAG = "HeightActivity";
    @Bind(R.id.ruler_height)
    RulerView ruler_height;
    @Bind(R.id.btn_done)
    LoadingButton btn_done;
    @Bind(R.id.tv_indicator)
    TextView tv_indicator;
    User user;
    String height;

    final static int MSG_CHANGE_HEIGHT = 0x1001;
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
                case MSG_CHANGE_HEIGHT:
                    //改变文字
                    int value = (int) ((float) msg.obj / 1);
                    tv_indicator.setText(value + "");
                    height = String.valueOf(value);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);
        initData();
        initView();
    }

    private void initData() {
        user = DataPreference.getUserInfo();
    }

    private void initView() {
        tv_indicator.setText(user.getHeight());

        final float heightF = Float.parseFloat(user.getHeight());
        ruler_height.setValue(heightF, 80, 250, 1);
        ruler_height.setStartColor(0xff44fc76);
        ruler_height.setEndColor(0xff0663f9);
        ruler_height.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                Log.i(TAG, "ruler_height value===>" + value);
                Message msg = new Message();
                msg.what = MSG_CHANGE_HEIGHT;
                msg.obj = value;
                handler.sendMessage(msg);
            }
        });

        //OnClick
        btn_done.setClickListener(new LoadingButton.ClickListener() {
            @Override
            public void onClick(View view) {
                //保存
                user.setHeight(height);
                DataPreference.setUserInfo(user);
                btn_done.setEnabled(false);
                user.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
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

    @OnClick({R.id.iv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_cancel:
                finish();
                break;
        }
    }
}
