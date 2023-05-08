package com.danotech.sevaa.Model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Savings {
    public String userID;
    public Double balance;
    public Double income;
    public Double expenses;
    public ArrayList<CreditCard> cards;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Savings() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Savings(String userID, Double income, Double expenses) {
        this.userID = userID;
        this.income = income;
        this.expenses = expenses;
    }

    public void save() {
        cards = new ArrayList<>();

        Map<String, Object> savings = new HashMap<>();
        savings.put("userID", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        savings.put("balance", calculateBalance(income, expenses));
        savings.put("income", income);
        savings.put("expenses", expenses);
//        savings.put("cards", Arrays.asList(cards).iterator());


        // Add a new document with a generated ID
        db.collection("savings").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .set(savings)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully added!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding to document", e));
    }

    public void update(Double income, Double expenses) {
        cards = new ArrayList<>();

        Map<String, Object> savings = new HashMap<>();
        savings.put("userID", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        savings.put("balance", calculateBalance(income, expenses));
        savings.put("income", income);
        savings.put("expenses", expenses);
//        savings.put("cards", Arrays.asList(cards));

        // Update new document with a generated ID
        db.collection("savings").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .update(savings)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    public void getUserSavingsInformation() {
        db.collection("savings")
                .whereEqualTo("userID", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (int i = 0; i < task.getResult().size(); i++) {
                            Log.d(TAG, task.getResult().getDocuments().get(i).getId() + " => " + task.getResult().getDocuments().get(i).getData());
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }).addOnFailureListener(e -> Log.w(TAG, "Error getting documents: ", e));
    }

    public void addCard(CreditCard card) {
        cards.add(card);
    }

    public void removeCard(CreditCard card) {
        cards.remove(card);
    }

    private Double calculateBalance(Double income, Double expenses) {
        balance = income - expenses;
        return balance;
    }

    private void calculateExpenses() {
        expenses = 0.0;
//        for (CreditCard card : cards) {
//            expenses += card.getBalance();
//        }
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public Double getExpenses() {
        return expenses;
    }

    public void setExpenses(Double expenses) {
        this.expenses = expenses;
    }

    public ArrayList<CreditCard> getCards() {
        return cards;
    }

    public void setCards(ArrayList<CreditCard> cards) {
        this.cards = cards;
    }
}
