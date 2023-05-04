package com.danotech.sevaa.Model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String firstName;
    public String lastName;
    public String email;
    public String DOB;
    public String password;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public User(String firstName, String lastName, String email, String DOB, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.DOB = DOB;
        this.password = password;
    }

    public void addUser() {
        // Read from the database
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first_name", firstName);
        user.put("last_name", lastName);
        user.put("born", DOB);
        user.put("email", email);
        user.put("password", password);

        // Add a new document with a generated ID
        db.collection("savings")
                .add(user)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    public void getSavingInfo() {
        db.collection("savings")
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getDOB() {
        return DOB;
    }

    public String getPassword() {
        return password;
    }
}
