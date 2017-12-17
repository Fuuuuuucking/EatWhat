package com.pulan.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pulan.eatwhat.LoginActivity;
import com.pulan.eatwhat.R;
import com.pulan.eatwhat.RegisterActivity;
import com.pulan.entity.User;
import com.pulan.util.CommonUtil;
import com.pulan.util.DataPreference;
import com.pulan.widget.NoScrollViewPager;
import com.pulan.widget.OverWatchLoadingView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by pulan on 17/11/19.
 */

public class PhoneNumFragment extends BaseFragment {

    private static final String TAG = "PhoneNumFragment";
    @Bind(R.id.btn_next)
    Button btn_next;
    @Bind(R.id.et_phoneNum)
    EditText et_phoneNum;
    String phoneNum;
    @Bind(R.id.loading_view)
    public OverWatchLoadingView loadingView;
    User user;

    final static int MSG_REQUESTSMS_SUCCESS = 0x1001;
    final static int MSG_REQUESTSM = 0x1002;//请求验证码
    final static int MSG_SYS_ERR = 0x1003;//系统错误，提示重试
    final static int MSG_ALREADY_REGISTER = 0x1004;//已注册
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ALREADY_REGISTER:
                    //显示对话框
                    CommonUtil.showCommonDialog(getActivity(),
                            getString(R.string.str_alreadyRegister),
                            getString(R.string.str_goLogin),
                            positiveListener);
                    //停止动画
                    loadingView.end();
                    //显示按钮
                    btn_next.setVisibility(View.VISIBLE);
                    break;
                case MSG_SYS_ERR:
                    //提示系统错误
                    CommonUtil.showToast(R.string.toast_sysErr);
                    //停止动画
                    loadingView.end();
                    //显示按钮
                    btn_next.setVisibility(View.VISIBLE);
                    break;
                case MSG_REQUESTSM:
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
                    break;
                case MSG_REQUESTSMS_SUCCESS:
                    //停止加载动画
                    loadingView.end();
                    //跳转下一步
                    NoScrollViewPager vp = RegisterActivity.getInstance().getVpRegister();
                    int childCount = vp.getAdapter().getCount();
                    int curIndex = vp.getCurrentItem();
                    if (curIndex < childCount - 1) {
                        vp.setCurrentItem(curIndex + 1);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.fragment_phonenum;
    }

    @Override
    protected void initAllViewMembers(Bundle savedInstanceState) {

    }

    DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //跳转登录页面
            Intent it = new Intent(getActivity(), LoginActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("phoneNum", phoneNum);
            if (user != null) {
                bundle.putSerializable("user", user);
            }
            it.putExtras(bundle);
            startActivity(it);
            //关闭页面
            getActivity().finish();
        }
    };

    @OnClick({R.id.btn_next, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                //跳转到登录页面
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.btn_next:
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
                    btn_next.setVisibility(View.GONE);
                    //开启加载动画
                    loadingView.start();
                    //检查下是否已经注册过
                    BmobQuery<User> query = new BmobQuery<>();
                    query.addWhereEqualTo("phoneNum", phoneNum);
                    query.setLimit(1);
                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if (e == null) {
                                Log.i(TAG, "查询成功");
                                if (list != null && list.size() > 0) {
                                    //已经注册过，提示是否跳转至登录页面
                                    PhoneNumFragment.this.user = list.get(0);
                                    handler.sendEmptyMessage(MSG_ALREADY_REGISTER);
                                } else {
                                    //没注册过，去请求验证码
                                    handler.sendEmptyMessage(MSG_REQUESTSM);
                                }
                            } else {
                                Log.i(TAG, "查询出错：" + e.toString());
                                handler.sendEmptyMessage(MSG_SYS_ERR);
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
}
