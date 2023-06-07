package com.danotech.sevaa.helpers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StorageReferenceFetcher implements DataFetcher<InputStream> {

    private static final String TAG = "StorageReferenceFetcher";

    private final StorageReference storageReference;
    private InputStream inputStream;

    public StorageReferenceFetcher(StorageReference storageReference) {
        this.storageReference = storageReference;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {
        storageReference.getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(result -> {
                    inputStream = new ByteArrayInputStream(result);
                    callback.onDataReady(inputStream);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load data from StorageReference", e);
                    callback.onLoadFailed(e);
                });
    }

    @Override
    public void cleanup() {
        // Close the input stream if it was opened
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e(TAG, "Failed to close input stream", e);
            }
        }
    }

    @Override
    public void cancel() {
        // No operation needed to cancel the load
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
}

