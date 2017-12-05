package com.pulan.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by admin on 2016/12/17.
 */
public class Food extends BmobObject {
    private String name;
    private boolean breakfast = false;
    private boolean lunch = false;
    private boolean dinner = false;
    private boolean snack = false;
    int energy;//包含的卡路里

    public Food() {
    }

    public boolean isSnack() {
        return snack;
    }

    public void setSnack(boolean snack) {
        this.snack = snack;
    }

    public Food(String name) {
        this.name = name;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBreakfast() {
        return breakfast;
    }

    public void setBreakfast(boolean breakfast) {
        this.breakfast = breakfast;
    }

    public boolean isLunch() {
        return lunch;
    }

    public void setLunch(boolean lunch) {
        this.lunch = lunch;
    }

    public boolean isDinner() {
        return dinner;
    }

    public void setDinner(boolean dinner) {
        this.dinner = dinner;
    }

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", breakfast=" + breakfast +
                ", lunch=" + lunch +
                ", dinner=" + dinner +
                ", snack=" + snack +
                ", energy=" + energy +
                '}';
    }
}
