package com.danotech.sevaa.UI.fragments;

import static androidx.core.app.ActivityCompat.recreate;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.danotech.sevaa.R;
import com.danotech.sevaa.helpers.ThemeManager;
import com.danotech.sevaa.helpers.ThemeUtils;

public class AppearanceSettings extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.account_preferences, rootKey);

        SwitchPreferenceCompat switchPreference = findPreference("theme");
        switchPreference.setOnPreferenceChangeListener(this);


        switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isDarkModeEnabled = (boolean) newValue;
                // Perform actions based on the new value (enable/disable dark mode, update UI, etc.)
                return true; // Return true to indicate that the change should be persisted
            }
        });

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals("theme")) {
            boolean isDarkModeEnabled = (Boolean) newValue;
            int newThemeMode = isDarkModeEnabled
                    ? AppCompatDelegate.MODE_NIGHT_YES
                    : AppCompatDelegate.MODE_NIGHT_NO;
            ThemeManager.setThemeMode(requireActivity(), newThemeMode);
            requireActivity().recreate();
        }
        return true;
    }
}