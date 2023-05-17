package com.danotech.sevaa.helpers;

import java.util.HashMap;

// TODO work the error in and issues with deserialization
public class DocumentClass {
    private HashMap<String, Object> hashMapField;

    public DocumentClass() {
        // Required empty constructor for Firebase deserialization
    }

    public DocumentClass(HashMap<String, Object> hashMapField) {
        this.hashMapField = hashMapField;
    }

    public HashMap<String, Object> getHashMapField() {
        return hashMapField;
    }

    public void setHashMapField(HashMap<String, Object> hashMapField) {
        this.hashMapField = hashMapField;
    }
}
