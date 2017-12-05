package com.pulan.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by pulan on 2017/9/28.
 */

public class Eated extends BmobObject {
    //吃过的食物
    Food food;
    //时间，格式为yyyy-MM-dd-HH-mm-ss
    String time;
    String eatTime;//用餐时间，格式为breakfast、lunch、dinner、snacks

    public Eated() {

    }

    public String getEatTime() {
        return eatTime;
    }

    public void setEatTime(String eatTime) {
        this.eatTime = eatTime;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Eated{" +
                "food=" + food +
                ", time='" + time + '\'' +
                ", eatTime='" + eatTime + '\'' +
                '}';
    }
}
