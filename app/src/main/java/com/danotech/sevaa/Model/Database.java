package com.danotech.sevaa.Model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Database {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public Database() {
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference("savings");
    }

    public void getConnection() {
        // Read from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Saving saving = dataSnapshot.getValue(Saving.class);
                System.out.println(saving);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                System.out.println("Failed to read value." + error.toException());
            }
        });
    }

    public void addUser(String userId, String username, String name,  String email, float expenses, float income, float balance) {
        Saving saving = new Saving(username, name, email, balance, expenses, email, income);
        databaseReference.child("savings").child(userId).setValue(saving);
    }
}
