package com.danotech.sevaa.Model;

import com.danotech.sevaa.helpers.CardType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class CreditCard {
    public static int NUMBER_OF_CARDS = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String number;
    private String expiryDate;
    private String cvv;
    private String name;
    private Boolean isPhysicalCard = false;
    private CardType cardType;

    public CreditCard() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        // this is required for firebase
        // do not remove
        number = "";
    }

    public CreditCard(String number, String expiryDate, String cvv, String name, Boolean isPhysicalCard, CardType cardType) {
        this.number = number;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.name = name;
        this.isPhysicalCard = isPhysicalCard;
        this.cardType = cardType;

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

    public void update(String number, String expiryDate, String cvv, String name, Boolean isPhysicalCard, CardType cardType) {
        this.number = number;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.name = name;
        this.isPhysicalCard = isPhysicalCard;
        this.cardType = cardType;

        Map<String, Object> card = new HashMap<>();
        card.put("name", this.name);
        card.put("number", this.number);
        card.put("expiry_date", this.expiryDate);
        card.put("cvv", this.cvv);
        card.put("is_physical_card", this.isPhysicalCard);
        card.put("type", this.cardType);

        // adds a new collection to the savings collection
        // contains all cards added by the user
        db.collection("savings")
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .collection("cards")
                .get().getResult().getDocuments().get(0).getReference().update(card);
    }

    public void delete(String cardNumber) {
        db.collection("savings")
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .collection("cards")
                .get()
                .getResult()
                .getDocuments()
                .get(0)
                .getReference()
                .delete();
    }

    public String getAllCards() {
        return db.collection("savings")
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .collection("cards").get().getResult().getDocuments().get(0).toString();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPhysicalCard() {
        return isPhysicalCard;
    }

    public void setPhysicalCard(Boolean physicalCard) {
        isPhysicalCard = physicalCard;
    }
}