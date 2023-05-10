package com.danotech.sevaa.UI.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.danotech.sevaa.IntroActivity;
import com.danotech.sevaa.R;


public class SecondIntroPageFragment extends Fragment {


    public SecondIntroPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second_page, container, false);
        Button nextPageButton = view.findViewById(R.id.second_page_next_button);
        Button skipButton = view.findViewById(R.id.second_page_skip_button);

        // send user back to second intro page
        nextPageButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), IntroActivity.class);
            intent.putExtra("page", 2);
            startActivity(intent);
        });

        // sends user to the third page
        // when user skips intro pages
        skipButton.setOnClickListener(V -> {
            Intent intent = new Intent(requireActivity(), IntroActivity.class);
            intent.putExtra("page", 2);
            startActivity(intent);
        });

        return view;
    }
}