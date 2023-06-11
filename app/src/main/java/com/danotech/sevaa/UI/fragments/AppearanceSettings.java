package com.danotech.sevaa.UI.fragments;

import static androidx.core.app.ActivityCompat.recreate;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.danotech.sevaa.R;
import com.danotech.sevaa.helpers.ThemeManager;
import com.danotech.sevaa.helpers.ThemeUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Set;

public class AppearanceSettings extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.account_preferences, rootKey);

        SwitchPreferenceCompat switchPreference = findPreference("theme");
        switchPreference.setOnPreferenceChangeListener(this);

        ListPreference preferenceCurrencyList = findPreference("currency");
        preferenceCurrencyList.setOnPreferenceChangeListener(this::onPreferenceChange);


        switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isDarkModeEnabled = (boolean) newValue;
                // Perform actions based on the new value (enable/disable dark mode, update UI, etc.)
                return true; // Return true to indicate that the change should be persisted
            }
        });


        Set<Currency> currencies = Currency.getAvailableCurrencies();

        int currencySize = currencies.size();

        CharSequence[] entries = new CharSequence[currencySize];
        CharSequence[] entryValues = new CharSequence[currencySize];

        int i = 0;
        for (Currency currency : currencies) {
            entries[i] = currency.getDisplayName();
            entryValues[i] = currency.getCurrencyCode();
            i++;
        }

        preferenceCurrencyList.setEntries(entries);
        preferenceCurrencyList.setEntryValues(entryValues);

        preferenceCurrencyList.setDefaultValue("$");
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