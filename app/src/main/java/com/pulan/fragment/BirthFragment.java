package com.pulan.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pulan.eatwhat.R;
import com.pulan.eatwhat.RegisterActivity;
import com.pulan.entity.User;
import com.pulan.util.DataPreference;
import com.pulan.widget.BirthView;
import com.pulan.widget.NoScrollViewPager;
import com.pulan.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by pulan on 17/11/21.
 */
public class BirthFragment extends BaseFragment {

    @Bind(R.id.btn_next)
    Button btn_next;
    @Bind(R.id.bv)
    BirthView bv;
    @Bind(R.id.tv_date)
    TextView tv_date;

    final static int MSG_CHANGE_DATE = 0x1001;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_CHANGE_DATE:
                    //获取日期
                    String yearSel = msg.getData().getString("yearSel");
                    String monthSel = msg.getData().getString("monthSel");
                    String daySel = msg.getData().getString("daySel");
                    tv_date.setText(String.format(getString(R.string.str_date),
                            yearSel, monthSel, daySel));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.fragment_birth;
    }

    @Override
    protected void initAllViewMembers(Bundle savedInstanceState) {
        tv_date.setText("1994年10月17日");
        //设置监听
        bv.setOnDateChangedListener(new BirthView.OnDateChangedListener() {
            @Override
            public void onDateChanged(String yearSel, String monthSel, String daySel) {
                Message msg = new Message();
                msg.what = MSG_CHANGE_DATE;
                Bundle bundle = new Bundle();
                bundle.putString("yearSel", yearSel);
                bundle.putString("monthSel", monthSel);
                bundle.putString("daySel", daySel);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        } else {

        }
    }

    @OnClick({R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                //本地存储
                String yearSel = bv.getYearSel();
                String monthSel = bv.getMonthSel();
                String daySel = bv.getDaySel();
                String birth = yearSel + "-" + monthSel + "-" + daySel;
                User user = DataPreference.getUserInfo();
                user.setBirth(birth);
                DataPreference.setUserInfo(user);
                //跳转
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
