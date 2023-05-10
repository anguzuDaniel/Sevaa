package com.danotech.sevaa.UI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.danotech.sevaa.Model.User;
import com.danotech.sevaa.R;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    private User user;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        user = new User();
        firebaseAuth = FirebaseAuth.getInstance();


        LinearLayout accountSettingsAction = view.findViewById(R.id.account_settings_action);
        accountSettingsAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) getActivity();

                // Launch the account settings fragment
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, new AccountSettingsFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}