package com.danotech.sevaa.UI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.danotech.sevaa.R;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

//        TextView userFirstName = view.findViewById(R.id.first_name);
//        TextView userSurName = view.findViewById(R.id.sur_name);
//        TextView ageTextView = view.findViewById(R.id.age);
//        TextView genderTextview = view.findViewById(R.id.gender);
//
//        if (this.getArguments() != null) {
//            String firstName = this.getArguments().getString("FirstName", "NO name");
//            String surName = this.getArguments().getString("Surname", "No sur name provided");
//            int age = this.getArguments().getInt("age", 0);
//            String gender = this.getArguments().getString("gender", "Male");
//
//            userFirstName.setText(firstName);
//            userSurName.setText(surName);
//            ageTextView.setText("You are " + age + " years old");
//            genderTextview.setText("You are a " + gender);
//        }


        // Inflate the layout for this fragment
        return view;
    }
}