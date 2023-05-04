package com.danotech.sevaa.UI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.danotech.sevaa.Model.User;
import com.danotech.sevaa.R;

public class HomeFragment extends Fragment {
    User user;
    TextView balance, income, expense, userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        balance = view.findViewById(R.id.balance);
        income = view.findViewById(R.id.income);
        expense = view.findViewById(R.id.expense);
        userName = view.findViewById(R.id.user_name);


        balance.setText("$0");
        income.setText("$0");
        expense.setText("$0");
        userName.setText("Daniel Anguzu");

        // Inflate the layout for this fragment
        return view;
    }
}