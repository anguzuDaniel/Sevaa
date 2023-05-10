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

import com.danotech.sevaa.IntroActivity;
import com.danotech.sevaa.R;
import com.danotech.sevaa.UI.LoginActivity;

public class FirstIntroPageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_first_page, container, false);
        Button nextPageButton = view.findViewById(R.id.first_page_next_button);
        Button skipButton = view.findViewById(R.id.first_page_skip_button);

        // transition to login activity
        // sets the page number to 1
        nextPageButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), IntroActivity.class);
            intent.putExtra("page", 1);
            startActivity(intent);
        });

        // transition to login activity
        // sets the page number to 2
        skipButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), IntroActivity.class);
            intent.putExtra("page", 2);
            startActivity(intent);
        });

        return view;
    }
}
