package com.danotech.sevaa.Model;

import java.util.ArrayList;

public class Savings {
    public String ID;
    public String name;
    public float balance;
    public float income;
    public float expenses;
    public ArrayList<CreditCard> cards;

    public Savings(String ID, String name, float balance, float income, float expenses, ArrayList<CreditCard> cards) {
        this.ID = ID;
        this.name = name;
        this.balance = balance;
        this.income = income;
        this.expenses = expenses;
        this.cards = cards;
    }

    public void addCard(CreditCard card) {
        cards.add(card);
    }

    public void removeCard(CreditCard card) {
        cards.remove(card);
    }

    public void addCard(String userID, String cardNumber, String expiryDate, String cvv) {
        CreditCard card = new CreditCard(userID, cardNumber, expiryDate, cvv);
        cards.add(card);
    }

    public void removeCard(String userID, String cardNumber, String expiryDate, String cvv) {
        CreditCard card = new CreditCard(userID, cardNumber, expiryDate, cvv);
        cards.remove(card);
    }

    public void calculateBalance(float income, float expenses) {
        balance = income - expenses;
    }
}
