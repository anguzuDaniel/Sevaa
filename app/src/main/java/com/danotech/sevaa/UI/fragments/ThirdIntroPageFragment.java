package com.danotech.sevaa.UI.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.danotech.sevaa.R;
import com.danotech.sevaa.UI.LoginActivity;
import com.danotech.sevaa.UI.SignUpActivity;

public class ThirdIntroPageFragment extends Fragment {
    private Button signInButton, signUpButton;
    Intent intent;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third_page, container, false);
        signInButton = view.findViewById(R.id.intro_sign_in_btn);
        signUpButton = view.findViewById(R.id.intro_sign_up_btn);

        // transition to login activity
        signInButton.setOnClickListener(v -> {
            intent = new Intent(requireActivity(), LoginActivity.class);
            startActivity(intent);
        });

        // transition to sign up activity
        signUpButton.setOnClickListener(v -> {
            intent = new Intent(requireActivity(), SignUpActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
