package com.pulan.eatwhat;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pulan.entity.Feedback;
import com.pulan.entity.User;
import com.pulan.util.CommonUtil;
import com.pulan.util.DataPreference;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by pulan on 17/10/25.
 */

public class FeedBackActivity extends BaseActivity {

    private static final String TAG = "FeedBackActivity";
    @Bind(R.id.et_feedback)
    EditText et_feedback;
    ProgressDialog dialog;
    User user;

    final static int MSG_UPLOAD_SUCCESS = 0x1001;//上传成功
    final static int MSG_UPLOAD_FAILED = 0x1002;//上传失败
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPLOAD_SUCCESS:
                    dialog.dismiss();
                    Toast.makeText(FeedBackActivity.this, "反馈成功", Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case MSG_UPLOAD_FAILED:
                    dialog.dismiss();
                    Toast.makeText(FeedBackActivity.this, "反馈失败", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        user = DataPreference.getUserInfo();
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在反馈...");
        //弹出键盘
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonUtil.showSoftKeyboard(et_feedback);
            }
        }, 100);
    }

    @Override
    public void finish() {
        //隐藏键盘
        CommonUtil.hideSoftInputFromWindow(this, et_feedback.getWindowToken());
        super.finish();
    }

    @OnClick({R.id.btn_feedback, R.id.iv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_cancel:
                finish();
                break;
            case R.id.btn_feedback:
                String strFeedback = et_feedback.getText().toString();
                if (strFeedback.equals("")) {
                    Toast.makeText(FeedBackActivity.this, "你倒是说啊", Toast.LENGTH_LONG).show();
                } else {
                    dialog.show();
                    Feedback feedback = new Feedback();
                    feedback.setUserNum(user.getPhoneNum());
                    feedback.setFeedback(strFeedback);
                    feedback.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Log.i(TAG, "上传成功");
                                handler.sendEmptyMessage(MSG_UPLOAD_SUCCESS);
                            } else {
                                Log.e(TAG, e.toString());
                                handler.sendEmptyMessage(MSG_UPLOAD_FAILED);
                            }
                        }
                    });
                }
                break;
        }
    }
}
