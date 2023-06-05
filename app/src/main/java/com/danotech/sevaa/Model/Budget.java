package com.danotech.sevaa.Model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Budget {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String title;
    private String name;
    private String date;

    public Budget() {
        // Do not remove
    }

    public Budget(String title, String name, String date) {
        this.title = title;
        this.name = name;
        this.date = date;
    }

    public void Save() {
        Map<String, Object> budget = new HashMap<>();
        budget.put("id", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        budget.put("title", this.title);
        budget.put("name", this.name);
        budget.put("date", this.date);

        db.collection("budget").document().set(budget).addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully added!")).addOnFailureListener(e -> Log.w(TAG, "Error adding to document", e));
    }

    public void update() {
        Map<String, Object> budget = new HashMap<>();
        budget.put("id", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        budget.put("title", this.title);
        budget.put("name", this.name);
        budget.put("date", this.date);

        db.collection("budget").document().update(budget).addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully added!")).addOnFailureListener(e -> Log.w(TAG, "Error adding to document", e));
    }

    public void getBudgets() {
        db.collection("budget")
                .whereEqualTo("id", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}
