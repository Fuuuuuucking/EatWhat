package com.pulan.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.pulan.eatwhat.R;
import com.pulan.eatwhat.RegisterActivity;
import com.pulan.entity.User;
import com.pulan.util.CommonUtil;
import com.pulan.util.DataPreference;
import com.pulan.widget.NoScrollViewPager;
import com.pulan.widget.SexSwitch;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by pulan on 17/11/19.
 */

public class SexFragment extends BaseFragment {
    private static final String TAG = "SexFragment";

    @Bind(R.id.switch_sex)
    SexSwitch switch_sex;
    boolean sex;
    User user;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_sex;
    }

    @Override
    protected void initAllViewMembers(Bundle savedInstanceState) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //隐藏软键盘
            CommonUtil.hideSoftKeyboard();
        }
    }

    @OnClick({R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                //获取性别
                sex = switch_sex.isChecked();
                if (sex == true) {
                    //女
                } else {
                    //男
                }
                //保存本地
                user = DataPreference.getUserInfo();
                user.setSex(sex);
                DataPreference.setUserInfo(user);
                NoScrollViewPager vp = RegisterActivity.getInstance().getVpRegister();
                int childCount = vp.getAdapter().getCount();
                Log.i(TAG, "childCount===>" + childCount);
                int curIndex = vp.getCurrentItem();
                if (curIndex < childCount - 1) {
                    vp.setCurrentItem(curIndex + 1);
                }
                break;
        }
    }
}
