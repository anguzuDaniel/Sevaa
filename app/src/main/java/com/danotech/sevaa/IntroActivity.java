package com.danotech.sevaa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.danotech.sevaa.R;
import com.danotech.sevaa.UI.Adapters.IntroPageAdapter;

public class IntroActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    IntroPageAdapter introPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        introPageAdapter = new IntroPageAdapter(this);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(introPageAdapter);
    }
}