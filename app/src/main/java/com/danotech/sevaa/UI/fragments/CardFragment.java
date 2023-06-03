package com.danotech.sevaa.UI.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.danotech.sevaa.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;


public class CardFragment extends Fragment {
    private TextView cardNum;
    private TextView cvvNum;
    private TextView cardName;
    private TextView expiryDate;
    private TextView cardType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card, container, false);

        cardNum = view.findViewById(R.id.card_number_text);
        cvvNum = view.findViewById(R.id.security_code_text);
        expiryDate = view.findViewById(R.id.expiry_date_text);
        cardName = view.findViewById(R.id.card_holder_text);
        cardType = view.findViewById(R.id.card_type_text);

        displayUserProfileInfo();

        return view;
    }

    public void displayUserProfileInfo() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String userEmail = currentUser.getEmail();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db
                .collection("savings")
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        documentReference
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> cardsMap = (Map<String, Object>) documentSnapshot.get("cards");
                        if (cardsMap != null) {
                            for (Map.Entry<String, Object> entry : cardsMap.entrySet()) {
                                String cardKey = entry.getKey();
                                Map<String, Object> cardDetails = (Map<String, Object>) entry.getValue();

                                cardName.setText(cardDetails.get("name").toString());
                                cardNum.setText(cardDetails.get("number").toString());
                                cardType.setText(cardDetails.get("type").toString());
                                cvvNum.setText(cardDetails.get("cvv").toString());
                                expiryDate.setText(cardDetails.get("expiry").toString());
                            }
                        } else {
                            // Handle case when the 'cards' field is null or not found
                            System.out.println("No cards found in the document.");
                        }
                    }
                }).addOnFailureListener(e -> {
                    // Handle any errors that occurred while fetching the document
                    System.out.println("Error getting document: " + e);
                });

    }
}