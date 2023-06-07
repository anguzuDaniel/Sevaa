package com.danotech.sevaa.Model;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.danotech.sevaa.R;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

public class ProfileHandler {
    private final StorageReference storageReference;
    private final ImageView imageViewProfile;

    public ProfileHandler(StorageReference storageReference, ImageView imageViewProfile) {
        this.storageReference = storageReference;
        this.imageViewProfile = imageViewProfile;
    }

    public void loadProfileImage(Context context, String profileImageUrl) {
        // Assuming you have a reference to the current user's image in Firebase Storage
        StorageReference profileImageRef = storageReference.child("images/" + profileImageUrl);


        // Load the image using Glide
        Glide.with(context)
                .load(profileImageRef)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.profile_default) // Placeholder image while loading
                        .error(R.drawable.profile_default) // Error image if loading fails
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(imageViewProfile);


        // Retrieve the download URL
        profileImageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    // Handle successful download
                    String downloadUrl = uri.toString();
                    // Use the download URL as needed
                })
                .addOnFailureListener(exception -> {
                    // Handle failure
                    if (exception instanceof StorageException && ((StorageException) exception).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                        // Object does not exist
                        Log.d(TAG, "Object does not exist at location");
                    } else {
                        // Other error occurred
                        Log.e(TAG, "Error getting download URL", exception);
                    }
                });
    }
}