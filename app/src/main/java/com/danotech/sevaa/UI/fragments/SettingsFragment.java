package com.danotech.sevaa.UI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.danotech.sevaa.R;

public class SettingsFragment extends Fragment {
    private String firstName;
    private String surName;
    private int age;
    private String gender;

    private TextView userFirstName, userSurName, ageTextView, genderTextview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        userFirstName = view.findViewById(R.id.first_name);
        userSurName = view.findViewById(R.id.sur_name);
        ageTextView = view.findViewById(R.id.age);
        genderTextview = view.findViewById(R.id.gender);

        if (this.getArguments() != null) {
            firstName = this.getArguments().getString("FirstName", "NO name");
            surName = this.getArguments().getString("Surname", "No sur name provided");
            age = this.getArguments().getInt("age", 0);
            gender = this.getArguments().getString("gender", "Male");

            userFirstName.setText(firstName);
            userSurName.setText(surName);
            ageTextView.setText("You are " + age + " years old");
            genderTextview.setText("You are a " + gender);
        }


        // Inflate the layout for this fragment
        return view;
    }
}