package com.danotech.sevaa.Model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class User {
    public String name;
    public String born;
    public String username;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        getUserInfo();
    }

    public User(String name, String born, String username) {
        this.name = name;
        this.born = born;
        this.username = username;
    }

    public void createProfile() {
        // Create a map with the updated fields
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        user.put("username", username);
        user.put("born", getBorn());

        // Update the document with the new data
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully added!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding to document", e));
    }

    public void updateProfile(String userID, String name, String username) {
        // create user with the following fields
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        user.put("username", username);
        user.put("born", getBorn());

        // Add a new document with a generated ID
        // Update the document with the new data
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .update(user)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    public void getUserInfo() {
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


    public Boolean hasProfile(String userID) {
        if (FirebaseAuth.getInstance() != null && FirebaseAuth.getInstance().getCurrentUser() != null) {
            String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            if (ID != null && ID.equals(userID)) {
                return true;
            }
        }

        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBorn() {
        return born;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
