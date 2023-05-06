package com.danotech.sevaa.Model;

import com.danotech.sevaa.UI.fragments.CreditCardFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CreditCard {
    public static int NUMBER_OF_CARDS;
    public String userID;
    public String cardNumber;
    public String expiryDate;
    public String cvv;

    public CreditCard(String userID, String cardNumber, String expiryDate, String cvv) {
        this.userID = userID;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;

        // increases whenever the constructor is called
        // this is used to show how many cards a user has
        NUMBER_OF_CARDS++;
    }

}