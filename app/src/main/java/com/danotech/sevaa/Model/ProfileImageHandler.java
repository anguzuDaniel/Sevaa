package com.danotech.sevaa.Model;

import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class ProfileImageHandler {
    private static final String PROFILE_IMAGES_FOLDER = "profile_images";

    // Firebase Storage reference
    private final StorageReference storageReference;

    public ProfileImageHandler() {
        // Initialize Firebase Storage
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public void uploadProfileImage(Uri imageUri, final OnProfileImageUploadListener listener) {
        // Generate a unique filename for the image
        String filename = UUID.randomUUID().toString();

        // Create a reference to the profile image file
        StorageReference riversRef = storageReference.child("images/" + filename);
        UploadTask uploadTask = riversRef.putFile(imageUri);

        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
            listener.onProfileImageUploadFailure(exception.getMessage());
        }).addOnSuccessListener(taskSnapshot -> {
            // Get the metadata for the uploaded image
            riversRef.getMetadata().addOnSuccessListener(storageMetadata -> {
                String imageUrl = storageMetadata.getName();
                // Save the upload URL in Firestore or perform any other desired operation

                // Invoke the callback to notify the success and provide the upload URL
                listener.onProfileImageUploadSuccess(filename);
            }).addOnFailureListener(e -> {
                // Handle unsuccessful uploads
                if (e instanceof StorageException) {
                    StorageException storageException = (StorageException) e;
                    if (storageException.getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                        // Handle object not found error
                        listener.onProfileImageUploadFailure("Object not found");
                        return;
                    }

                    if (storageException.getErrorCode() == StorageException.ERROR_BUCKET_NOT_FOUND) {
                        // Handle permission denied error
                        listener.onProfileImageUploadFailure("Permission denied");
                        return;
                    }
                }

                // Handle other errors
                listener.onProfileImageUploadFailure(e.getMessage());
            });
        });
    }

    // Interface for handling profile image upload callbacks
    public interface OnProfileImageUploadListener {
        void onProfileImageUploadSuccess(String imageUrl);

        void onProfileImageUploadFailure(String errorMessage);
    }

    private String getFileExtension(ContentResolver contentResolver, Uri uri) {
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private boolean isFileExtensionValid(String extension) {
        // Define the valid file extensions
        String[] validExtensions = {"jpg", "jpeg", "png"};

        // Check if the provided extension is valid
        for (String validExtension : validExtensions) {
            if (validExtension.equalsIgnoreCase(extension)) {
                return true;
            }
        }

        return false;
    }

    public void uploadFile(ContentResolver contentResolver, Uri fileUri, final OnProfileImageUploadListener listener) {
        String fileExtension = getFileExtension(contentResolver, fileUri);

        if (fileExtension != null && isFileExtensionValid(fileExtension)) {
            // Continue with the upload process
            uploadProfileImage(fileUri, listener);
        } else {
            // Handle invalid file extension
        }
    }
}
