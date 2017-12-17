package com.pulan.eatwhat;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pulan.entity.User;
import com.pulan.util.DataPreference;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by pulan on 17/11/26.
 */

public class MineActivity extends BaseActivity {

    User user;
    @Bind(R.id.tv_phoneNum)
    TextView tv_phoneNum;
    @Bind(R.id.iv_sex)
    ImageView iv_sex;

    @Bind(R.id.tv_bestEnergy)
    TextView tv_bestEnergy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        initData();
        initView();
    }

    private void initView() {
        if (user != null && !user.getPhoneNum().equals("")) {
            String phoneNum = user.getPhoneNum();
            tv_phoneNum.setText(phoneNum.substring(0, 3) + "****" + phoneNum.substring(7, phoneNum.length()));
            if (user.getSex() == true) {
                //女
                iv_sex.setImageResource(R.drawable.img_sex_woman);
            } else {
                //男
                iv_sex.setImageResource(R.drawable.img_sex_man);
            }
            tv_bestEnergy.setText(user.getREE() + "");
        }
    }

    private void initData() {
        //获取本地用户信息
        user = DataPreference.getUserInfo();
    }

    @OnClick({R.id.iv_cancel, R.id.ll_weight, R.id.ll_height})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_weight:
                start2Activity(WeightActivity.class);
                break;
            case R.id.ll_height:
                start2Activity(HeightActivity.class);
                break;
            case R.id.iv_cancel:
                finish();
                break;
        }
    }
}
