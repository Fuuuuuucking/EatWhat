package com.pulan.eatwhat;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pulan.entity.User;
import com.pulan.util.CommonUtil;
import com.pulan.util.DataPreference;
import com.pulan.widget.OverWatchLoadingView;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by pulan on 17/11/26.
 */

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    @Bind(R.id.et_phoneNum)
    EditText et_phoneNum;
    @Bind(R.id.et_code)
    EditText et_code;
    @Bind(R.id.btn_login)
    Button btn_login;
    @Bind(R.id.btn_getCode)
    Button btn_getCode;
    @Bind(R.id.loading_view)
    OverWatchLoadingView loadingView;

    String phoneNum, confirmCode;
    //60s倒计时
    CountDownTimer downTimer;

    final static int MSG_REQUESTSMS_SUCCESS = 0x1001;//发送验证码成功
    final static int MSG_VERIFY_SUCCESS = 0x1002;//验证验证码成功
    final static int MSG_VERIFY_FAILED = 0x1003;//验证验证码失败
    final static int MSG_RECEIVE_SMS_TIMETICK = 0x1004;//走时
    final static int MSG_RECEIVE_SMS_TIMEOUT = 0x1005;//倒计时完毕
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_VERIFY_SUCCESS:
                    //验证成功即登录成功
                    CommonUtil.showToast(R.string.toast_loginSuccess);
                    loadingView.end();
                    start2Activity(MainActivity.class);
                    finish();
                    break;
                case MSG_VERIFY_FAILED:
                    CommonUtil.showToast(R.string.toast_confirmCodeErr);
                    btn_login.setVisibility(View.VISIBLE);
                    loadingView.end();
                    break;
                case MSG_RECEIVE_SMS_TIMEOUT:
                    btn_getCode.setText(R.string.str_getCode);
                    btn_getCode.setEnabled(true);
                    break;
                case MSG_RECEIVE_SMS_TIMETICK:
                    int time = (int) ((long) msg.obj / 1000);
                    btn_getCode.setText(time + "s");
                    break;
                case MSG_REQUESTSMS_SUCCESS:
                    //开启重试倒计时
                    downTimer = new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            //走时
                            Message msg = new Message();
                            msg.obj = millisUntilFinished;
                            msg.what = MSG_RECEIVE_SMS_TIMETICK;
                            handler.sendMessage(msg);
                        }

                        @Override
                        public void onFinish() {
                            //倒计时结束，可以重发验证码
                            handler.sendEmptyMessage(MSG_RECEIVE_SMS_TIMEOUT);
                        }
                    };
                    downTimer.start();
                    //按钮不可用
                    btn_getCode.setEnabled(false);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downTimer != null) {
            downTimer.cancel();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initData();
        initView();
    }

    private void initData() {
        phoneNum = getIntent().getStringExtra("phoneNum");
    }

    private void initView() {
        if (phoneNum != null && !phoneNum.equals("")) {
            //自动填写手机号
            et_phoneNum.setText(phoneNum);
        }
    }

    @OnClick({R.id.btn_getCode, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                //检查网络
                //check NET
                if (CommonUtil.isNetworkAvailable() == false) {
                    CommonUtil.showToast(R.string.toast_badNet);
                    return;
                }
                //获取电话号码
                phoneNum = et_phoneNum.getText().toString().trim();
                if (!phoneNum.equals("") && CommonUtil.isPhoneNum(phoneNum)) {
                    //本地存储一下
                    User user = DataPreference.getUserInfo();
                    if (user == null) {
                        user = new User();
                    }
                    user.setPhoneNum(phoneNum);
                    DataPreference.setUserInfo(user);
                    //隐藏自己
                    btn_login.setVisibility(View.GONE);
                    //开启加载动画
                    loadingView.start();
                    //验证验证码
                    confirmCode = et_code.getText().toString().trim();
                    if (confirmCode.length() != 6) {
                        //提示输入6位验证码
                        CommonUtil.showToast(R.string.toast_confirmCodeErr);
                    } else {
                        //去验证验证码是否正确
                        verifySMSCode();
                    }
                } else {
                    //提示电话号码有误
                    CommonUtil.showToast(R.string.toast_phoneNumIsErr);
                }
                break;
            case R.id.btn_getCode:
                //check NET
                if (CommonUtil.isNetworkAvailable() == false) {
                    CommonUtil.showToast(R.string.toast_badNet);
                    return;
                }
                //获取电话号码
                phoneNum = et_phoneNum.getText().toString().trim();
                if (!phoneNum.equals("") && CommonUtil.isPhoneNum(phoneNum)) {
                    //请求验证码
                    BmobSMS.requestSMSCode(phoneNum, "自定义", new QueryListener<Integer>() {
                        @Override
                        public void done(Integer smsId, BmobException e) {
                            if (e == null) {
                                //验证码发送成功
                                Log.i(TAG, "短信id：" + smsId);//用于查询本次短信发送详情
                                handler.sendEmptyMessage(MSG_REQUESTSMS_SUCCESS);
                            } else {
                                Log.i(TAG, "请求失败:" + e.toString());
                            }
                        }
                    });
                } else {
                    //提示电话号码有误
                    CommonUtil.showToast(R.string.toast_phoneNumIsErr);
                }

                break;
        }
    }

    /**
     * 验证短信验证码
     */
    private void verifySMSCode() {
        BmobSMS.verifySmsCode(phoneNum, confirmCode, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //验证通过
                    handler.sendEmptyMessage(MSG_VERIFY_SUCCESS);
                } else {
                    //验证失败
                    handler.sendEmptyMessage(MSG_VERIFY_FAILED);
                }
            }
        });
    }
}
