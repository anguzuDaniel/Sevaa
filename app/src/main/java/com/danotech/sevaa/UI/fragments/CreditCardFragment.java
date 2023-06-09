package com.danotech.sevaa.UI.fragments;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.danotech.sevaa.Model.Budget;
import com.danotech.sevaa.Model.CreditCard;
import com.danotech.sevaa.R;
import com.danotech.sevaa.helpers.CardType;
import com.danotech.sevaa.helpers.DateConversion;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Map;

public class CreditCardFragment extends Fragment {
    private BottomSheetDialog bottomSheetDialog;
    private CreditCard creditCard;
    View cardView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_credit_card, container, false);

        Context context = getContext();

        RelativeLayout openBottomSheetButton = view.findViewById(R.id.button_add_card);
        openBottomSheetButton.setOnClickListener(v -> showBottomSheet(context));


        displayUserProfileInfo(view);
        displayBudgetInformation(view);

        ExtendedFloatingActionButton fab = view.findViewById(R.id.extended_fab);
        fab.setOnClickListener(v -> {
            showBottomSheetBudget(context);
        });

        return view;
    }


    private void showBottomSheet(Context context) {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(getLayoutInflater()
                    .inflate(R.layout.activity_bottom_sheet, null));
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

    private void showBottomSheetBudget(Context context) {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(getLayoutInflater()
                    .inflate(R.layout.fragment_bottom_sheet_budget, null));
            bottomSheetDialog.setCanceledOnTouchOutside(true); // Allow the user to dismiss the bottom sheet by tapping outside it


            Button submitButton = bottomSheetDialog.findViewById(R.id.button_submit);
            assert submitButton != null;
            submitButton.setOnClickListener(v -> {
                // Get credit card information from form
                EditText budgetTitleText = bottomSheetDialog.findViewById(R.id.budget_title);
                EditText budgetNameText = bottomSheetDialog.findViewById(R.id.budget_name);


                assert budgetTitleText != null;
                String budgetTitle = budgetTitleText.getText().toString();
                String budgetName = budgetNameText.getText().toString();

                String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());

                Budget budget = new Budget(budgetTitle, budgetName, timeStamp);
                budget.Save();

                Toast.makeText(getContext(), "Budget added", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });
        }

        bottomSheetDialog.show();
    }

    private boolean validateCreditCard(
            String cardNumber,
            String expiryDate,
            String cvv,
            String cardName) {
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

        CreditCard creditCard = new CreditCard(cardNumber, expiryDate, cvv, cardName, true, CardType.VISA);
        creditCard.save();

        return true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayBudgetInformation(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        displayBudgetInformation(getView());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bottomSheetDialog = null;
    }

    public void displayUserProfileInfo(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String userEmail = currentUser.getEmail();

        // Assuming you have a LinearLayout with id "cardContainer" in your XML layout
        // container that will displays the cards
        LinearLayout cardContainer = view.findViewById(R.id.cardContainer);

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

                                // Inflate a new card layout for each card
                                View cardView = getLayoutInflater().inflate(R.layout.fragment_card, null);


                                TextView cardNum = cardView.findViewById(R.id.card_number_text);
                                TextView cvvNum = cardView.findViewById(R.id.security_code_text);
                                TextView expiryDate = cardView.findViewById(R.id.expiry_date_text);
                                TextView cardName = cardView.findViewById(R.id.card_holder_text);
                                TextView cardType = cardView.findViewById(R.id.card_type_text);


                                // Set the card details
                                cardName.setText(name);
                                cardNum.setText(number);
                                cardType.setText(type);
                                cvvNum.setText(cvv);
                                expiryDate.setText(expiry);

                                // Add the card layout to the cardContainer
                                cardContainer.setOrientation(LinearLayout.HORIZONTAL);
                                cardContainer.addView(cardView);
                            }

                        } else {
                            TextView textView = view.findViewById(R.id.no_card_added_message);
                            textView.setText("No cards added yet");
                            cardContainer.addView(textView);
                        }
                    }
                }).addOnFailureListener(e -> {
                    // Handle any errors that occurred while fetching the document
                    System.out.println("Error getting document: " + e);
                });
    }

    public void displayBudgetInformation(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("budget")
                .whereEqualTo("id", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            LinearLayout budgetContainer = view.findViewById(R.id.budget_container);

                            String title = document.getString("title") != null ? document.getString("title") : "";
                            String name = document.getString("name") != null ? document.getString("name") : "";
                            String date = document.getString("date") != null ? document.getString("date") : "";

                            View budgetCard = getLayoutInflater().inflate(R.layout.budget_card, null);

                            TextView titleText = budgetCard.findViewById(R.id.budget_title);
                            TextView nameText = budgetCard.findViewById(R.id.budget_name);
                            TextView dateText = budgetCard.findViewById(R.id.budget_date);

                            titleText.setText(title);
                            nameText.setText(name);
                            dateText.setText(DateConversion.convert(date));

                            budgetContainer.addView(budgetCard);

                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}