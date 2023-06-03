package com.danotech.sevaa.UI.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.danotech.sevaa.Model.User;
import com.danotech.sevaa.R;
import com.danotech.sevaa.UI.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    private User user;
    private FirebaseAuth firebaseAuth;
    LinearLayout accountSettingsAction, logoutAction, cardSettingAction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        user = new User();
        firebaseAuth = FirebaseAuth.getInstance();


        accountSettingsAction = view.findViewById(R.id.account_settings_action);
        cardSettingAction = view.findViewById(R.id.account_card_settings);
        logoutAction = view.findViewById(R.id.account_logout_action);

        accountSettingsAction.setOnClickListener(v -> {
            // Launch the account settings fragment
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_frame, new AccountFragment())
                    .addToBackStack(null)
                    .commit();
        });

        cardSettingAction.setOnClickListener(v -> {
            // Launch the card fragment
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.account_card_settings, new AccountFragment())
                    .addToBackStack(null)
                    .commit();
        });

        logoutAction.setOnClickListener(v -> {
            firebaseAuth.signOut();

            startActivity(new Intent(getActivity(), LoginActivity.class));
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}