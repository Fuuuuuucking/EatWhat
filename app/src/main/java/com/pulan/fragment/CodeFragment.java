package com.pulan.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pulan.eatwhat.R;
import com.pulan.eatwhat.RegisterActivity;
import com.pulan.util.CommonUtil;
import com.pulan.util.DataPreference;
import com.pulan.widget.NoScrollViewPager;
import com.pulan.widget.OverWatchLoadingView;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by pulan on 17/11/19.
 * 验证码输入页面
 */

public class CodeFragment extends BaseFragment {

    private static final String TAG = "CodeFragment";
    @Bind(R.id.btn_next)
    Button btn_next;
    @Bind(R.id.et_code)
    EditText et_code;
    String codeNum;
    @Bind(R.id.tv_retry)
    TextView tv_retry;
    String confirmCode;
    String phoneNum;
    @Bind(R.id.loading_view)
    OverWatchLoadingView loadingView;

    //60s倒计时
    CountDownTimer downTimer;

    final static int MSG_RECEIVE_SMS_TIMEOUT = 0x1001;//接收验证码超时
    final static int MSG_RECEIVE_SMS_TIMETICK = 0x1002;//接收验证码走时
    final static int MSG_VERIFY_SUCCESS = 0x1003;//验证验证码成功
    final static int MSG_VERIFY_FAILED = 0x1004;//验证验证码失败
    final static int MSG_REQUESTSMS_SUCCESS = 0x1005;//发送验证码成功
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_REQUESTSMS_SUCCESS:
                    tv_retry.setEnabled(false);
                    //开启倒计时
                    downTimer = new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
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

                    break;
                case MSG_VERIFY_SUCCESS:
                    loadingView.end();
                    //进入下一步
                    NoScrollViewPager vp = RegisterActivity.getInstance().getVpRegister();
                    int childCount = vp.getAdapter().getCount();
                    int curIndex = vp.getCurrentItem();
                    if (curIndex < childCount - 1) {
                        vp.setCurrentItem(curIndex + 1);
                    }
                    break;
                case MSG_VERIFY_FAILED:
                    CommonUtil.showToast(R.string.toast_confirmCodeErr);
                    btn_next.setVisibility(View.VISIBLE);
                    loadingView.end();
                    break;
                case MSG_RECEIVE_SMS_TIMETICK:
                    String str = getString(R.string.str_receiveSMSTick);
                    int time = (int) ((long) msg.obj / 1000);
                    tv_retry.setText(String.format(str, time + ""));
                    break;
                case MSG_RECEIVE_SMS_TIMEOUT:
                    tv_retry.setText(R.string.str_registerRetry);
                    tv_retry.setEnabled(true);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.fragment_code;
    }

    @Override
    protected void initAllViewMembers(Bundle savedInstanceState) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            phoneNum = DataPreference.getUserInfo().getPhoneNum();
            tv_retry.setEnabled(false);
            //开启倒计时
            downTimer = new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
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
        } else {
            if (downTimer != null) {
                downTimer.cancel();
            }
        }
    }

    @OnClick({R.id.btn_next, R.id.tv_retry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                confirmCode = et_code.getText().toString().trim();
                if (confirmCode.length() != 6) {
                    //提示输入6位验证码
                    CommonUtil.showToast(R.string.toast_confirmCodeErr);
                } else {
                    //隐藏自己
                    btn_next.setVisibility(View.GONE);
                    //显示加载动画
                    loadingView.start();
                    //去验证验证码是否正确
                    verifySMSCode();
                }
                break;
            case R.id.tv_retry:
                sendSMS();
                break;
        }
    }

    /**
     * 发送验证码短信
     */
    private void sendSMS() {
        BmobSMS.requestSMSCode(phoneNum, "自定义", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {
                    //验证码发送成功
                    Log.i(TAG, "短信id：" + smsId);//用于查询本次短信发送详情
                    handler.sendEmptyMessage(MSG_REQUESTSMS_SUCCESS);
                }
            }
        });
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
