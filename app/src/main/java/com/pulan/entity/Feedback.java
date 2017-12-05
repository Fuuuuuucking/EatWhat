package com.pulan.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by pulan on 17/10/25.
 */

public class Feedback extends BmobObject {
    String deviceid;//设备唯一号
    String feedback;//反馈内容
    String userNum;//用户编号，现在暂时为手机号

    public Feedback() {
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }
}
