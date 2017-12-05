package com.pulan.eatwhat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pulan.util.PuLanUtil;
import com.suke.widget.SwitchButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pulan on 2017/9/24.
 */
public class SettingActivity extends BaseActivity {

    @Bind(R.id.sb_notification)
    SwitchButton sb_notification;
    @Bind(R.id.tv_version)
    TextView tv_version;
    @Bind(R.id.ll_version)
    LinearLayout ll_version;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {
        try {
            tv_version.setText(PuLanUtil.getAppVersion(getPackageName()) + "." + PuLanUtil.getVersionCode(this));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.ll_feedback, R.id.ll_about, R.id.ll_version, R.id.ll_mine,
            R.id.iv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_cancel:
                finish();
                break;
            case R.id.ll_mine:
                start2Activity(MineActivity.class);
                break;
            case R.id.ll_feedback:
                //跳转至反馈页面
                Intent it = new Intent(SettingActivity.this, FeedBackActivity.class);
                startActivity(it);
                break;
            case R.id.ll_about:
                //跳转至联系页面
                Intent itAbout = new Intent(SettingActivity.this, AboutActivity.class);
                startActivity(itAbout);
                break;
            case R.id.ll_version:
                Toast.makeText(SettingActivity.this, "你真好看", Toast.LENGTH_LONG).show();
                break;

        }
    }
}
