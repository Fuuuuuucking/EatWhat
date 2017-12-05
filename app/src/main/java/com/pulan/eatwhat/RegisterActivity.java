package com.pulan.eatwhat;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.pulan.adapter.MyFragmentAdapter;
import com.pulan.fragment.BirthFragment;
import com.pulan.fragment.CodeFragment;
import com.pulan.fragment.HeightFragment;
import com.pulan.fragment.PhoneNumFragment;
import com.pulan.fragment.SexFragment;
import com.pulan.fragment.WeightFragment;
import com.pulan.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class RegisterActivity extends BaseActivity {

    @Bind(R.id.vp_register)
    public NoScrollViewPager vp_register;
    MyFragmentAdapter adapter;
    List<Fragment> fragments;


    public static RegisterActivity instance;

    public static RegisterActivity getInstance() {
        return instance;
    }

    public NoScrollViewPager getVpRegister() {
        return vp_register;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        instance = this;
        initView();
    }

    private void initView() {
        fragments = new ArrayList<>();

        PhoneNumFragment phoneNumFragment = new PhoneNumFragment();
        CodeFragment codeFragment = new CodeFragment();
        SexFragment sexFragment = new SexFragment();
        BirthFragment birthFragment = new BirthFragment();
        HeightFragment heightFragment = new HeightFragment();
        WeightFragment weightFragment = new WeightFragment();

        fragments.add(phoneNumFragment);
        fragments.add(codeFragment);
        fragments.add(sexFragment);
        fragments.add(birthFragment);
        fragments.add(heightFragment);
        fragments.add(weightFragment);

        adapter = new MyFragmentAdapter(this, fragments, getSupportFragmentManager());
        vp_register.setAdapter(adapter);
    }
}
