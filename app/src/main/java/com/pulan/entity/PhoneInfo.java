package com.pulan.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by pulan on 17/11/24.
 */

public class PhoneInfo extends BmobObject {

    /**
     * 是否是第一次打开app
     */
    boolean isFisrtOpen;

    public PhoneInfo() {
        this.isFisrtOpen = true;
    }

    public boolean isFisrtOpen() {
        return isFisrtOpen;
    }

    public void setFisrtOpen(boolean fisrtOpen) {
        isFisrtOpen = fisrtOpen;
    }
}
