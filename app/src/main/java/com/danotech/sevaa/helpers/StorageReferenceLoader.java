package com.danotech.sevaa.helpers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

public class StorageReferenceLoader implements ModelLoader<StorageReference, InputStream> {

    private final Context context;

    public StorageReferenceLoader(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public LoadData<InputStream> buildLoadData(@NonNull StorageReference storageReference, int width, int height, @NonNull Options options) {
        return new LoadData<>(new GlideUrl(storageReference.getDownloadUrl().toString()), new StorageReferenceFetcher(storageReference));
    }

    @Override
    public boolean handles(@NonNull StorageReference storageReference) {
        return true;
    }

    public static class StorageReferenceLoaderFactory implements ModelLoaderFactory<StorageReference, InputStream> {
        private Context context;

        public StorageReferenceLoaderFactory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ModelLoader<StorageReference, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
            return new StorageReferenceLoader(context);
        }

        @Override
        public void teardown() {
            // No cleanup required
        }
    }
}
