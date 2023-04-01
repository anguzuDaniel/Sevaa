package com.example.appbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.appbar.UI.fragments.MyFragment;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Fragment myFragment = new MyFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, myFragment).commit();
    }
}