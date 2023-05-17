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

import com.danotech.sevaa.Model.CreditCard;
import com.danotech.sevaa.Model.Savings;
import com.danotech.sevaa.Model.User;
import com.danotech.sevaa.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {
    User user;
    Savings savings;
    CreditCard creditCard;
    TextView balance, income, expense, userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        user = new User();
        creditCard = new CreditCard();
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
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String userEmail = currentUser.getEmail();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("savings")
                .whereEqualTo("userID", userEmail)
                .get()
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                    return FirebaseFirestore.getInstance().collection("savings").document(userEmail).get();
                })
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        savings = documentSnapshot.toObject(Savings.class);
                        creditCard = documentSnapshot.toObject(CreditCard.class);

                        balance.setText("$" + savings.getBalance());
                        income.setText("$" + savings.getIncome());
                        expense.setText("$" + savings.getExpenses());

                        System.out.println(creditCard);
                    }
                })
                .addOnFailureListener(e -> {
                    balance.setText("$" + 0.0);
                    income.setText("$" + 0.0);
                    expense.setText("$" + 0.0);
                    Log.d(TAG, "onFailure: " + e);
                });

    }
}