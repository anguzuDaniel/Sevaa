package com.danotech.sevaa.UI.fragments;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.danotech.sevaa.Model.User;
import com.danotech.sevaa.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class SettingsFragment extends Fragment {

    private User user;
    private FirebaseAuth firebaseAuth;
    private View firstName;
    private View lastName;
    private View username;
    private String DOB;
    private Button updateProfileButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firstName = view.findViewById(R.id.profile_first_name);
        lastName = view.findViewById(R.id.profile_last_name);
        username = view.findViewById(R.id.profile_username);
        updateProfileButton = view.findViewById(R.id.update_profile_button);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        showDatePickerDialog(view);
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = firebaseAuth.getCurrentUser().getUid();
                user = new User();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                progressBar.setVisibility(View.VISIBLE); // shows a spinner while the profile is being updated

                if (!user.hasProfile(userID)) {
                    user.createProfile(userID, firstName.toString(), lastName.toString(), username.toString());
                } else {
                    user.updateProfile(userID, firstName.toString(), lastName.toString(), username.toString());
                }

                // Add a listener to hide the progress bar when the update operation is complete
                db.collection("users").document(userID)
                        .addSnapshotListener((documentSnapshot, e) -> {
                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e);
                                return;
                            }

                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + documentSnapshot.getData());
                                progressBar.setVisibility(View.GONE); // hide progress bar here
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        });
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Fragment accountSettingsFragment = new AccountSettingsFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.account_profile_setting, accountSettingsFragment).commit();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void showDatePickerDialog(View view) {
        // Get a reference to the button in your layout
        Button datePickerButton = view.findViewById(R.id.date_picker_button);

        // Set a click listener for the button
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date as the default date for the dialog
                Calendar currentDate = Calendar.getInstance();
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int day = currentDate.get(Calendar.DAY_OF_MONTH);

                // Create a DatePickerDialog with the current date as the default
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        SettingsFragment.this.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Update your UI with the selected date
                                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;

                                if (user != null) {
                                    // Update your model with the selected date
                                    user.setDOB(selectedDate);
                                }
                            }
                        },
                        year,
                        month,
                        day
                );

                // Set the maximum date to today's date to prevent users from selecting future dates
                datePickerDialog.getDatePicker().setMaxDate(currentDate.getTimeInMillis());

                // Show the dialog
                datePickerDialog.show();
            }
        });
    }
}