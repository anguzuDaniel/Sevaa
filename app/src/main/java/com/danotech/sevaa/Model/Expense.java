package com.danotech.sevaa.Model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.danotech.sevaa.helpers.ExpenseType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Expense {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String name;
    private ExpenseType expenseType;
    private String price;
    private String date;

    public Expense() {
        // Default constructor required for calls to DataSnapshot.getValue(Expense.class)
        // this is required for firebase
        // do not remove
    }

    public Expense(String name, ExpenseType expenseType, String price, String date) {
        this.name = name;
        this.expenseType = expenseType;
        this.price = price;
        this.date = date;
    }

    public void Save() {
        Map<String, Object> budget = new HashMap<>();
        budget.put("id", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        budget.put("name", this.name);
        budget.put("expense_type", this.expenseType.name());
        budget.put("price", this.price);
        budget.put("date", this.date);

        db.collection("expense")
                .document()
                .set(budget)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully added!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding to document", e));
    }

    public void update() {
        Map<String, Object> budget = new HashMap<>();
        budget.put("id", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        budget.put("name", this.name);
        budget.put("expense_type", this.expenseType.name());
        budget.put("price", this.price);
        budget.put("date", this.date);

        db.collection("expense").document().update(budget).addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully added!")).addOnFailureListener(e -> Log.w(TAG, "Error adding to document", e));
    }
}
