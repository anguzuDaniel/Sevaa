package com.danotech.sevaa.UI.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.danotech.sevaa.R;

public class CardSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}