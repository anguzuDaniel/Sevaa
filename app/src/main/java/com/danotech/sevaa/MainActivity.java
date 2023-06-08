package com.danotech.sevaa;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.danotech.sevaa.UI.fragments.CreditCardFragment;
import com.danotech.sevaa.UI.fragments.HomeFragment;
import com.danotech.sevaa.UI.fragments.SettingsFragment;
import com.danotech.sevaa.helpers.ThemeManager;
import com.danotech.sevaa.helpers.ThemeUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Apply the stored theme mode
        int themeMode = ThemeManager.getThemeMode(this);
        AppCompatDelegate.setDefaultNightMode(themeMode);

        // Obtain the SharedPreferences instance
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Create the preference change listener
        preferenceChangeListener = (sharedPrefs, key) -> {
            if (key.equals("theme")) {
                boolean isDarkModeEnabled = sharedPreferences.getBoolean(key, false);
                updateBackgroundColors(isDarkModeEnabled);
            }
        };

        // Register the preference change listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        // navigation bars
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        replaceFragment(new HomeFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.cards:
                    replaceFragment(new CreditCardFragment());
                    break;
                case R.id.more:
                    replaceFragment(new SettingsFragment());
                    break;
            }
            return true;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister the preference change listener
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    private void updateBackgroundColors(boolean isDarkModeEnabled) {
        List<View> backgroundViews = ThemeUtils.findAllViewsById(getWindow().getDecorView(), R.id.background);
        for (View backgroundView : backgroundViews) {
            ThemeUtils.updateBackgroundColor(backgroundView, isDarkModeEnabled);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame, fragment);
        fragmentTransaction.commit();
    }
}