package com.danotech.sevaa.helpers;

import static androidx.core.app.ActivityCompat.recreate;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {
    private static final String THEME_PREFS = "theme_prefs";
    private static final String THEME_MODE_KEY = "theme_mode";

    public static void setThemeMode(Context context, int themeMode) {
        SharedPreferences prefs = context.getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(THEME_MODE_KEY, themeMode);
        editor.apply();
    }

    public static int getThemeMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE);
        return prefs.getInt(THEME_MODE_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }
}
