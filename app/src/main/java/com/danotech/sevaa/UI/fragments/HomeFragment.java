package com.danotech.sevaa.UI.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.danotech.sevaa.Model.Savings;
import com.danotech.sevaa.Model.User;
import com.danotech.sevaa.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class HomeFragment extends Fragment {
    User user;
    Savings savings;
    TextView balance, income, expense, userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        user = new User();
        balance = view.findViewById(R.id.balance);
        income = view.findViewById(R.id.income);
        expense = view.findViewById(R.id.expense);
        userName = view.findViewById(R.id.user_name);


        displayUserProfileInfo();

        if (user != null && user.hasProfile(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail())) {
            userName.setText(user.getUsername());
        } else {
            userName.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
        }

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("balance", balance.getText().toString());
        outState.putString("income", income.getText().toString());
        outState.putString("expense", expense.getText().toString());
        outState.putString("userName", userName.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            balance.setText(savedInstanceState.getString("balance"));
            income.setText(savedInstanceState.getString("income"));
            expense.setText(savedInstanceState.getString("expense"));
            userName.setText(savedInstanceState.getString("userName"));
        }
    }

    public void displayUserProfileInfo() {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("savings").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                savings = documentSnapshot.toObject(Savings.class);
                balance.setText("$" + savings.getBalance());
                income.setText("$" + savings.getIncome());
                expense.setText("$" + savings.getExpenses());
            }
        }).addOnFailureListener(e -> {
            balance.setText("$" + 0.0);
            income.setText("$" + 0.0);
            expense.setText("$" + 0.0);
            Log.d(TAG, "onFailure: " + e);
        });
    }
}