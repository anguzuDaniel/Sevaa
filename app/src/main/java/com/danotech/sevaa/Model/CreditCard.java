package com.danotech.sevaa.Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class CreditCard {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static int NUMBER_OF_CARDS = 0;
    private String number;
    private String expiryDate;
    private String cvv;
    private String name;
    private Boolean isPhysicalCard = false;
    private final String[] types = {"Visa", "Mastercard", "American Express"};
    private String cardType;

    // used to get what card is being used by calling types[cardType]
    public static final int VISA = 0;
    public static final int MASTERCARD = 1;
    public static final int AMERICAN_EXPRESS = 2;

    public CreditCard() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        // this is required for firebase
        // do not remove
        number = "";
    }

    public CreditCard(String number, String expiryDate, String cvv, String name, Boolean isPhysicalCard, int type) {
        this.number = number;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.name = name;
        this.isPhysicalCard = isPhysicalCard;

        // convert this to a switch statement
        switch (type) {
            case VISA:
                cardType = types[0];
                break;
            case MASTERCARD:
                cardType = types[1];
                break;
            case AMERICAN_EXPRESS:
                cardType = types[2];
                break;
            default:
                cardType = "Unknown";
                break;
        }


        NUMBER_OF_CARDS++; // increments when a card is created
    }

    public void save() {
        Map<String, Object> card = new HashMap<>();
        card.put("name", this.name);
        card.put("number", this.number);
        card.put("expiry_date", this.expiryDate);
        card.put("cvv", this.cvv);
        card.put("is_physical_card", this.isPhysicalCard);
        card.put("type", this.cardType);

        Map<String, Object> saving = new HashMap<>();
        saving.put(this.number, card);

        // adds a new card collection
        // contains all cards added by the user
        db.collection("savings")
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .set(new HashMap<String, Object>() {{
                    put("cards", saving);
                }}, SetOptions.merge());
    }

    public void update(String number, String expiryDate, String cvv, String name, Boolean isPhysicalCard, int type) {
        this.number = number;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.name = name;
        this.isPhysicalCard = isPhysicalCard;

        // convert this to a switch statement
        switch (type) {
            case VISA:
                cardType = types[0];
                break;
            case MASTERCARD:
                cardType = types[1];
                break;
            case AMERICAN_EXPRESS:
                cardType = types[2];
                break;
            default:
                cardType = "Unknown";
                break;
        }

        Map<String, Object> card = new HashMap<>();
        card.put("name", this.name);
        card.put("number", this.number);
        card.put("expiry_date", this.expiryDate);
        card.put("cvv", this.cvv);
        card.put("is_physical_card", this.isPhysicalCard);
        card.put("type", this.cardType);

        // adds a new collection to the savings collection
        // contains all cards added by the user
        db.collection("savings").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("cards").get().getResult().getDocuments().get(0).getReference().update(card);
    }

    public void delete(String cardNumber) {
        db.collection("savings").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("cards").get().getResult().getDocuments().get(0).getReference().delete();
    }

    public String getAllCards() {
        return db.collection("savings").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("cards").get().getResult().getDocuments().get(0).toString();
    }
}