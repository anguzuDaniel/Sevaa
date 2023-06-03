package com.danotech.sevaa.UI.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.danotech.sevaa.Model.CreditCard;
import com.danotech.sevaa.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class CreditCardFragment extends Fragment {
    private BottomSheetDialog bottomSheetDialog;
    private CreditCard creditCard;
    private TextView cardNum;
    private TextView cvvNum;
    private TextView cardName;
    private TextView expiryDate;
    private TextView cardType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_credit_card, container, false);

        // Inflate the layout for this fragment

//        Intent intent = new Intent(getActivity(), CardSettingsActivity.class);
//        startActivity(intent);

        creditCard = new CreditCard();

        cardNum = view.findViewById(R.id.card_number_text);
        cvvNum = view.findViewById(R.id.security_code_text);
        expiryDate = view.findViewById(R.id.expiry_date_text);
        cardName = view.findViewById(R.id.card_holder_text);
        cardType = view.findViewById(R.id.card_type_text);

        Context context = getContext();

        Button openBottomSheetButton = view.findViewById(R.id.button_add_card);
        openBottomSheetButton.setOnClickListener(v -> showBottomSheet(context));

        displayUserProfileInfo();

        return view;
    }


    private void showBottomSheet(Context context) {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(getLayoutInflater().inflate(R.layout.activity_bottom_sheet, null));
            bottomSheetDialog.setCanceledOnTouchOutside(true); // Allow the user to dismiss the bottom sheet by tapping outside it


            Button submitButton = bottomSheetDialog.findViewById(R.id.button_submit);
            assert submitButton != null;
            submitButton.setOnClickListener(v -> {
                // Get credit card information from form
                EditText cardNumberEditText = bottomSheetDialog.findViewById(R.id.edit_text_card_number);
                EditText expiryDateEditText = bottomSheetDialog.findViewById(R.id.edit_text_expiration_date);
                EditText cvvEditText = bottomSheetDialog.findViewById(R.id.edit_text_cvv);
                EditText cardNameText = bottomSheetDialog.findViewById(R.id.edit_text_card_name);

                assert cardNumberEditText != null;
                String cardNumber = cardNumberEditText.getText().toString();
                String expiryDate = expiryDateEditText.getText().toString();
                assert cvvEditText != null;
                String cvv = cvvEditText.getText().toString();
                String cardName = cardNameText.getText().toString();

                // Validate credit card information
                boolean isValid = validateCreditCard(cardNumber, expiryDate, cvv, cardName);

                if (isValid) {
                    // Process payment
                    Toast.makeText(getContext(), "Payment information add successfully", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                } else {
                    // Show error message
                    Toast.makeText(getContext(), "Invalid credit card information", Toast.LENGTH_SHORT).show();
                }
            });
        }

        bottomSheetDialog.show();
    }

    private boolean validateCreditCard(String cardNumber, String expiryDate, String cvv, String cardName) {
        // TODO: Implement credit card validation logic
        if (TextUtils.isEmpty(cardNumber)) {
            return false;
        }

        if (TextUtils.isEmpty(expiryDate)) {
            return false;
        }

        if (TextUtils.isEmpty(cvv)) {
            return false;
        }

        CreditCard creditCard = new CreditCard(cardNumber, expiryDate, cvv, cardName, true, CreditCard.VISA);
        creditCard.save();

        return true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Fragment fragment = new CardSettingsFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

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

                                String name = cardDetails.get("name") != null ? cardDetails.get("name").toString() : "";
                                String number = cardDetails.get("number") != null ? cardDetails.get("number").toString() : "";
                                String type = cardDetails.get("type") != null ? cardDetails.get("type").toString() : "";
                                String cvv = "cvv: " + (cardDetails.get("cvv") != null ? cardDetails.get("cvv").toString() : "");
                                String expiry = "Exp Date: " + (cardDetails.get("expiry_date") != null ? cardDetails.get("expiry_date").toString() : "");

                                if (cardDetails != null) {
                                    cardName.setText(name);
                                    cardNum.setText(number);
                                    cardType.setText(type);
                                    cvvNum.setText(cvv);
                                    expiryDate.setText(expiry);
                                } else {
                                    // Handle case when the cardDetails map is null
                                    System.out.println("No card details found.");
                                }

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