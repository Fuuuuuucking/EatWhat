package com.pulan.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by pulan on 17/11/19.
 */

public class User extends BmobObject {

    /**
     * 电话号码
     */
    String phoneNum;
    /**
     * 性别
     */
    boolean sex;//true==开=女，false==关=男
    /**
     * 出生日期
     */
    String birth;//格式yyyy-MM-dd
    /**
     * 身高cm
     */
    String height;
    /**
     * 体重kg
     */
    String weight;

    /**
     * 每日最佳摄入能量
     */
    int REE;

    /**
     * 吃过的历史数据
     */
    String eatedJsonStr;

    public User() {
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public boolean getSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getREE() {
        return REE;
    }

    public void setREE(int REE) {
        this.REE = REE;
    }

    public String getEatedJsonStr() {
        return eatedJsonStr;
    }

    public void setEatedJsonStr(String eatedJsonStr) {
        this.eatedJsonStr = eatedJsonStr;
    }
}
