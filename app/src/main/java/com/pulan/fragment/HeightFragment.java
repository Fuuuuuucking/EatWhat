package com.pulan.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pulan.eatwhat.R;
import com.pulan.eatwhat.RegisterActivity;
import com.pulan.entity.User;
import com.pulan.util.DataPreference;
import com.pulan.widget.NoScrollViewPager;
import com.pulan.widget.RulerView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by pulan on 17/11/21.
 */
public class HeightFragment extends BaseFragment {

    private static final String TAG = "HeightFragment";
    @Bind(R.id.ruler_height)
    RulerView ruler_height;
    @Bind(R.id.tv_indicator)
    TextView tv_indicator;
    String height;

    final static int MSG_CHANGE_HEIGHT = 0x1001;//改变海拔事件
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
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
    public int getContentViewId() {
        return R.layout.fragment_height;
    }

    @Override
    protected void initAllViewMembers(Bundle savedInstanceState) {
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
        ruler_height.setValue(172, 80, 250, 1);
        ruler_height.setStartColor(0xff44fc76);
        ruler_height.setEndColor(0xff0663f9);
        height = "172";
    }

    @OnClick({R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                //保存本地
                User user = DataPreference.getUserInfo();
                user.setHeight(height);
                DataPreference.setUserInfo(user);
                //跳转下一页
                NoScrollViewPager vp = RegisterActivity.getInstance().getVpRegister();
                int childCount = vp.getAdapter().getCount();
                int curIndex = vp.getCurrentItem();
                if (curIndex < childCount - 1) {
                    vp.setCurrentItem(curIndex + 1);
                }
                break;
        }
    }
}
