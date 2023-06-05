package com.danotech.sevaa.UI.fragments;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.danotech.sevaa.Model.CreditCard;
import com.danotech.sevaa.Model.Expense;
import com.danotech.sevaa.Model.Savings;
import com.danotech.sevaa.Model.User;
import com.danotech.sevaa.R;
import com.danotech.sevaa.helpers.ExpenseType;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class HomeFragment extends Fragment {
    User user;
    Savings savings;
    CreditCard creditCard;
    TextView balance, income, expense, userName;
    private BottomSheetDialog bottomSheetDialog;
    ExpenseType expenseType;


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

        Context context = getContext();
        ExtendedFloatingActionButton fab = view.findViewById(R.id.extended_fab_expense);

        fab.setOnClickListener(v -> {
            showBottomSheetBudget(context);
        });

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

    private void showBottomSheetBudget(Context context) {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(getLayoutInflater()
                    .inflate(R.layout.fragment_bottom_sheet_expense, null));
            bottomSheetDialog.setCanceledOnTouchOutside(true); // Allow the user to dismiss the bottom sheet by tapping outside it


            Button submitButton = bottomSheetDialog.findViewById(R.id.button_submit);
            assert submitButton != null;
            submitButton.setOnClickListener(v -> {
                // Get credit card information from form
                EditText expenseTitleText = bottomSheetDialog.findViewById(R.id.expense_name);
                EditText expenseNameText = bottomSheetDialog.findViewById(R.id.expense_price);
                AutoCompleteTextView autoCompleteTextView = bottomSheetDialog.findViewById(R.id.expense_type);


                assert expenseTitleText != null;
                String expenseName = expenseTitleText.getText().toString();
                String expensePrice = expenseNameText.getText().toString();

                String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());

                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Get the selected item's ID
                        long selectedId = id;

                        expenseType = ExpenseType.getExpenseTypeById(id);
                        Expense expense = new Expense(expenseName, expenseType, expensePrice, timeStamp);
                        expense.Save();
                    }
                });



                Toast.makeText(getContext(), "Budget added", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });
        }

        bottomSheetDialog.show();
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