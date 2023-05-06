package com.danotech.sevaa.Model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class User {
    public String userID;
    public String firstName;
    public String lastName;
    public String email;
    public String DOB;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public User() {

    }

    public void createProfile(String userID, String firstName, String lastName, String username) {
        // Create a map with the updated fields
        Map<String, Object> user = new HashMap<>();
        user.put("name", Arrays.asList(firstName, lastName));
        user.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        user.put("Username", username);
        user.put("born", getDOB());

        // Update the document with the new data
        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully added!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding to document", e));
    }

    public void updateProfile(String userID, String firstName, String lastName, String username) {
        // create user with the following fields
        Map<String, Object> user = new HashMap<>();
        user.put("name", Arrays.asList(firstName, lastName));
        user.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        user.put("Username", username);
        user.put("born", getDOB());

        // Add a new document with a generated ID
        // Update the document with the new data
        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .update(user)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    public void getSavingInfo() {
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            System.out.println(document.getData());
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public Boolean hasProfile(String userID) {

        if (FirebaseAuth.getInstance() != null && FirebaseAuth.getInstance().getCurrentUser() != null) {
            String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            if (ID != null && ID.equals(userID)) {
                return true;
            }
        }

        return false;
    }

    public String getDOB() {
        return DOB;
    }
}
