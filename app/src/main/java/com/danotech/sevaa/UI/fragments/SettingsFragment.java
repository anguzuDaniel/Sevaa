package com.danotech.sevaa.UI.fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.danotech.sevaa.Model.ProfileHandler;
import com.danotech.sevaa.Model.User;
import com.danotech.sevaa.R;
import com.danotech.sevaa.UI.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SettingsFragment extends Fragment {

    private User user;
    private FirebaseAuth firebaseAuth;
    LinearLayout accountSettingsAction, logoutAction, cardSettingAction;
    ImageView imageView;
    private StorageReference storageReference;
    TextView emailTextView, usernameTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        user = new User();
        firebaseAuth = FirebaseAuth.getInstance();


        accountSettingsAction = view.findViewById(R.id.account_settings_action);
        cardSettingAction = view.findViewById(R.id.account_appearance_action);
        logoutAction = view.findViewById(R.id.account_logout_action);
        emailTextView = view.findViewById(R.id.email);
        usernameTextView = view.findViewById(R.id.user_name);

        storageReference = FirebaseStorage.getInstance().getReference();

        accountSettingsAction.setOnClickListener(v -> {
            // Launch the account settings fragment
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_frame, new AccountFragment())
                    .addToBackStack(null)
                    .commit();
        });

        cardSettingAction.setOnClickListener(v -> {
            // Launch the card fragment
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_frame, new AppearanceSettings())
                    .addToBackStack(null)
                    .commit();
        });

        logoutAction.setOnClickListener(v -> {
            firebaseAuth.signOut();

            startActivity(new Intent(getActivity(), LoginActivity.class));
        });

        displayProfileInfo(view);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void displayProfileInfo(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        imageView = view.findViewById(R.id.profile_image);


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
                    ProfileHandler profileHandler = new ProfileHandler(storageReference, imageView);
                    profileHandler.loadProfileImage(requireContext(), document.getString("profileImageUrl"));

                    usernameTextView.setText(document.get("name").toString());
                    emailTextView.setText(document.get("email").toString());
                }
            } else {
                Log.d(TAG, "Failed to retrieve user document from the users collection", task.getException());
            }
        });
    }
}