package com.danotech.sevaa;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.danotech.sevaa.UI.Adapters.IntroPageAdapter;

public class IntroActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    IntroPageAdapter introPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        int page = getIntent().getIntExtra("page", 0);

        introPageAdapter = new IntroPageAdapter(this);
        viewPager = findViewById(R.id.intro_view_pager);
        viewPager.setAdapter(introPageAdapter);
        viewPager.setCurrentItem(page);

        // set the current page based on the page number
        if (page == 1) {
            viewPager.setCurrentItem(0);
        } else if (page == 2) {
            viewPager.setCurrentItem(1);
        }
    }
}