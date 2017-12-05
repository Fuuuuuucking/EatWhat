package com.pulan.adapter;

import com.pulan.entity.Eated;
import com.pulan.entity.Food;

import java.util.List;

/**
 * Created by pupu on 2017/9/23.
 */

public class HistoryModel {

    List<Eated> breakfastFoods;
    List<Eated> lunchFoods;
    List<Eated> dinnerFoods;
    List<Eated> snackFoods;
    String dateTime;//日期时间：格式为：今天、前天、昨天(today、yesterday、preyesterday、mm月dd日)

    public HistoryModel() {

    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<Eated> getBreakfastFoods() {
        return breakfastFoods;
    }

    public void setBreakfastFoods(List<Eated> breakfastFoods) {
        this.breakfastFoods = breakfastFoods;
    }

    public List<Eated> getLunchFoods() {
        return lunchFoods;
    }

    public void setLunchFoods(List<Eated> lunchFoods) {
        this.lunchFoods = lunchFoods;
    }

    public List<Eated> getDinnerFoods() {
        return dinnerFoods;
    }

    public void setDinnerFoods(List<Eated> dinnerFoods) {
        this.dinnerFoods = dinnerFoods;
    }

    public List<Eated> getSnackFoods() {
        return snackFoods;
    }

    public void setSnackFoods(List<Eated> snackFoods) {
        this.snackFoods = snackFoods;
    }

    @Override
    public String toString() {
        return "HistoryModel{" +
                "breakfastFoods=" + breakfastFoods +
                ", lunchFoods=" + lunchFoods +
                ", dinnerFoods=" + dinnerFoods +
                ", snackFoods=" + snackFoods +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
