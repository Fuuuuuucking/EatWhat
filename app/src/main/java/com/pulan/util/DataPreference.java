package com.pulan.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulan.app.MyApplication;
import com.pulan.entity.Eated;
import com.pulan.entity.Food;
import com.pulan.entity.PhoneInfo;
import com.pulan.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pulan on 2017/9/28.
 * 数据库辅助类
 */

public class DataPreference {
    static String TAG = "DataPreference";

    /**
     * 保存手机相关的信息
     *
     * @param info
     */
    public static void setPhoneInfo(PhoneInfo info) {
        SharedPreferences preferences = MyApplication.getInstance()
                .getSharedPreferences("phoneInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String tmp = gson.toJson(info);
        editor.putString("jsonInfo", tmp);
        editor.commit();
    }

    /**
     * 获取手机相关信息
     *
     * @return
     */
    public static PhoneInfo getPhoneInfo() {
        PhoneInfo info = new PhoneInfo();
        SharedPreferences preferences = MyApplication.getInstance()
                .getSharedPreferences("phoneInfo", Context.MODE_PRIVATE);
        String tmp = preferences.getString("jsonInfo", "");
        if (!tmp.equals("")) {
            Gson gson = new Gson();
            info = gson.fromJson(tmp, PhoneInfo.class);
            return info;
        }
        return info;
    }

    /**
     * 本地保存用户数据
     *
     * @param user
     */
    public static void setUserInfo(User user) {
        SharedPreferences preferences = MyApplication.getInstance()
                .getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String tmp = gson.toJson(user);
        editor.putString("jsonInfo", tmp);
        editor.commit();
    }

    /**
     * 获取本地用户数据
     *
     * @return
     */
    public static User getUserInfo() {
        User user = new User();
        SharedPreferences preferences = MyApplication.getInstance()
                .getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String tmp = preferences.getString("jsonInfo", "");
        if (!tmp.equals("")) {
            Gson gson = new Gson();
            user = gson.fromJson(tmp, User.class);
            return user;
        }
        return user;
    }

    /**
     * 获取所有食物
     *
     * @return
     */
    public static List<Food> getAllFoods() {
        List<Food> list = null;
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences("allFoods", Context.MODE_PRIVATE);
        String tmp = preferences.getString("jsonInfo", "");
        if (!tmp.equals("")) {
            Gson gson = new Gson();
            list = gson.fromJson(tmp, new TypeToken<List<Food>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 设置所有食物待选列表
     *
     * @param list
     */
    public static void setAllFoods(List<Food> list) {
        //转json
        Gson gson = new Gson();
        String tmp = gson.toJson(list);
        setAllFoods(tmp);
    }

    /**
     * 设置所有食物待选列表
     *
     * @param jsonStr
     */
    public static void setAllFoods(String jsonStr) {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences("allFoods", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("jsonInfo", jsonStr);
        editor.commit();
    }

    /**
     * 获取早餐食物待选项
     *
     * @return
     */
    public static List<Food> getFoodBreakfast() {
        List<Food> list = new ArrayList<>();
        List<Food> listAll = getAllFoods();
        for (Food food : listAll) {
            if (food.isBreakfast() == true) {
                list.add(food);
            }
        }
        return list;
    }

    /**
     * 获取中餐食物待选项
     *
     * @return
     */
    public static List<Food> getFoodLunch() {
        List<Food> list = new ArrayList<>();
        List<Food> listAll = getAllFoods();
        for (Food food : listAll) {
            if (food.isLunch() == true) {
                list.add(food);
            }
        }
        return list;
    }

    /**
     * 获取晚餐食物待选项
     *
     * @return
     */
    public static List<Food> getFoodDinner() {
        List<Food> list = new ArrayList<>();
        List<Food> listAll = getAllFoods();
        for (Food food : listAll) {
            if (food.isLunch() == true) {
                list.add(food);
            }
        }
        return list;
    }

    /**
     * 获取零食食物待选项
     *
     * @return
     */
    public static List<Food> getFoodSnack() {
        List<Food> list = new ArrayList<>();
        List<Food> listAll = getAllFoods();
        for (Food food : listAll) {
            if (food.isSnack() == true) {
                list.add(food);
            }
        }
        return list;
    }

    /**
     * 添加一条吃过的数据
     *
     * @param eated
     */
    public static void addEated(Eated eated) {
        //获取所有的历史记录
        List<Eated> list = getAllEated();
        if (list == null || list.size() == 0) {
            list = new ArrayList<>();
        }
        //插入到最前
        list.add(0, eated);
//        list.add(eated);
        //存储
        setAllEated(list);
    }

    /**
     * 获取所有吃过的历史记录
     *
     * @return
     */
    public static List<Eated> getAllEated() {
        List<Eated> list = null;
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences("allEated", Context.MODE_PRIVATE);
        String tmp = preferences.getString("jsonInfo", "");
        if (!tmp.equals("")) {
            Gson gson = new Gson();
            list = gson.fromJson(tmp, new TypeToken<List<Eated>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 获取json数据集，String形式返回
     *
     * @return
     */
    public static String getAllEatedJsonStr() {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences("allEated", Context.MODE_PRIVATE);
        String tmp = preferences.getString("jsonInfo", "");
        return tmp;
    }

    /**
     * 设置吃过的历史
     *
     * @param jsonStr
     */
    public static void setAllEatedJsonStr(String jsonStr) {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences("allEated", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        //转json
        if (jsonStr == null) {
            editor.putString("jsonInfo", "");
        } else {
            editor.putString("jsonInfo", jsonStr);
        }
        editor.commit();
    }

    /**
     * 存储吃过的历史记录列表
     *
     * @param list
     */
    public static void setAllEated(List<Eated> list) {
        SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences("allEated", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        //转json
        Gson gson = new Gson();
        String tmp = gson.toJson(list);
        editor.putString("jsonInfo", tmp);
        editor.commit();
    }
}
