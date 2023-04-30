package com.danotech.sevaa.UI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.danotech.sevaa.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class CreditCardFragment extends Fragment {
    private BottomSheetDialog bottomSheetDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_credit_card, container, false);

        // Inflate the layout for this fragment

//        Intent intent = new Intent(getActivity(), CardSettingsActivity.class);
//        startActivity(intent);

        Button openBottomSheetButton = view.findViewById(R.id.button_add_card);
        openBottomSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();
            }
        });

        return view;
    }


    private void showBottomSheet() {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(getContext());
            bottomSheetDialog.setContentView(R.layout.activity_bottom_sheet);

            Button submitButton = bottomSheetDialog.findViewById(R.id.button_submit);
            assert submitButton != null;
            submitButton.setOnClickListener(v -> {
                // Get credit card information from form
                EditText cardNumberEditText = bottomSheetDialog.findViewById(R.id.edit_text_card_number);
                EditText expiryDateEditText = bottomSheetDialog.findViewById(R.id.edit_text_expiration_date);
                EditText cvvEditText = bottomSheetDialog.findViewById(R.id.edit_text_cvv);

                assert cardNumberEditText != null;
                String cardNumber = cardNumberEditText.getText().toString();
                String expiryDate = expiryDateEditText.getText().toString();
                assert cvvEditText != null;
                String cvv = cvvEditText.getText().toString();

                // Validate credit card information
                boolean isValid = validateCreditCard(cardNumber, expiryDate, cvv);

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

    private boolean validateCreditCard(String cardNumber, String expiryDate, String cvv) {
        // TODO: Implement credit card validation logic
        return true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Fragment fragment = new CardSettingsFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.card_setting_options, fragment).commit();
    }
}