package com.danotech.sevaa.UI.fragments;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.danotech.sevaa.Model.ProfileHandler;
import com.danotech.sevaa.Model.ProfileImageHandler;
import com.danotech.sevaa.Model.User;
import com.danotech.sevaa.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.TimeZone;

public class AccountFragment extends Fragment implements ProfileImageHandler.OnProfileImageUploadListener {
    private User user;
    private FirebaseAuth firebaseAuth;
    private EditText name;
    private EditText lastName;
    private EditText username;

    private String DOB;
    private Button updateProfileButton;
    private ImageView updateProfileImageBtn;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    private Uri selectedImageUri;
    private ProfileImageHandler profileImageHandler;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        user = new User();
        firebaseAuth = FirebaseAuth.getInstance();
        name = view.findViewById(R.id.profile_name);
        username = view.findViewById(R.id.profile_username);
        updateProfileButton = view.findViewById(R.id.update_profile_button);
        updateProfileImageBtn = view.findViewById(R.id.edit_profile_image_btn);

        storageReference = FirebaseStorage.getInstance().getReference();

        profileImageView = view.findViewById(R.id.profile_image);
        profileImageHandler = new ProfileImageHandler();

        updateProfileImageBtn.setOnClickListener(v -> openImagePicker());

        // Initialize the image picker launcher
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                selectedImageUri = result.getData().getData();

                Glide.with(this)
                        .load(selectedImageUri)
                        .centerCrop()
                        .into(profileImageView);
            }
        });


        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        // date picker button
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build();

        progressBar.setVisibility(View.GONE);

        displayUserProfileInfo();

        // Get a reference to the button in your layout
        Button datePickerButton = view.findViewById(R.id.date_picker_button);

        // Set a click listener for the button
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(view);
            }
        });

        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = firebaseAuth.getCurrentUser().getUid();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                progressBar.setVisibility(View.VISIBLE); // shows a spinner while the profile is being updated


                // Create a reference to the user document
                DocumentReference userRef = db
                        .collection("users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getEmail());

                // Call the get() method on the reference to retrieve the document
                userRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            user.updateProfile(userID, name.getText().toString(), username.getText().toString());
                            db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
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

                            updateProfileImage();

                            Log.d(TAG, "User exists in the users collection");
                        } else {
                            // User document exists in the users collection
                            String name = AccountFragment.this.name.getText().toString();
                            user = new User(name, "1997", username.getText().toString());
                            user.createProfile();
                            Log.d(TAG, "User does not exist in the users collection");
                        }
                    } else {
                        Log.d(TAG, "Failed to retrieve user document from the users collection", task.getException());
                    }
                });
            }
        });

        return view;
    }


    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(Intent.createChooser(intent, "Select Image"));
    }

    private void updateProfileImage() {
        if (selectedImageUri != null) {
            profileImageHandler.uploadFile(requireActivity().getContentResolver(), selectedImageUri, this);
        } else {
            Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProfileImageUploadSuccess(String imageUrl) {
        // Update the user's profile image URL in the database or perform any other necessary actions
        Toast.makeText(getContext(), "Profile image updated successfully", Toast.LENGTH_SHORT).show();

        // Update the profile image URL in Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // using the email of the logged in user to be able to get
        DocumentReference userRef = db
                .collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        // Update the 'profileImageUrl' field with the new image URL
        userRef.update("profileImageUrl", imageUrl)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Profile image updated successfully!");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error updating document", e);
                });
    }


    @Override
    public void onProfileImageUploadFailure(String errorMessage) {
        Toast.makeText(getContext(), "Failed to update profile image: " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    public void getDOB() {
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build();

        MaterialDatePicker.Builder.datePicker().setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);

        datePicker.show(getActivity().getSupportFragmentManager(), "tag");

        // Get today's date in UTC milliseconds.
        long today = MaterialDatePicker.todayInUtcMilliseconds();

        // Create a calendar instance in UTC time zone and set its time to today's date.
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(today);

        // Set the calendar to January of the current year.
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        long janThisYear = calendar.getTimeInMillis();

        // Set the calendar to December of the current year.
        calendar.setTimeInMillis(today);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        long decThisYear = calendar.getTimeInMillis();

        // Build the calendar constraints.
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder()
                .setStart(janThisYear)
                .setEnd(decThisYear);
    }

    private void showDatePickerDialog(View view) {
        // Get the current date as the default date for the dialog
        Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog with the current date as the default
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AccountFragment.this.getContext(),
                (DatePickerDialog.OnDateSetListener) (view1, year1, month1, dayOfMonth) -> {
                    // Update your UI with the selected date
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;

                    if (user != null) {
                        // Update your model with the selected date
                        user.setBorn(selectedDate);
                    }
                },
                year,
                month,
                day
        );

        // Set the maximum date to today's date to prevent users from selecting future dates
        datePickerDialog
                .getDatePicker()
                .setMaxDate(currentDate.getTimeInMillis());

        // Show the dialog
        datePickerDialog.show();
    }

    // displays users information if user has already created a profile
    public void displayUserProfileInfo() {
        String userID = firebaseAuth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a reference to the user document
        DocumentReference userRef = db
                .collection("users")
                .document(FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getEmail());

        // Call the get() method on the reference to retrieve the document
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // User document exists in the users collection
                    name.setText(document.getString("name"));
                    username.setText(document.getString("username"));

                    // User document exists in the users collection
                    ProfileHandler profileHandler = new ProfileHandler(storageReference, profileImageView);
                    profileHandler.loadProfileImage(requireContext(), document.getString("profileImageUrl"));

                } else {
                    // User document does not exist in the users collection
                    name.setHint("First name");
                    lastName.setHint("Last name");
                    username.setHint("Username");
                }
            } else {
                Log.d(TAG, "Failed to retrieve user document from the users collection", task.getException());
            }
        });
    }
}