package com.danotech.sevaa.Model;

public class Saving {
    public String name;
    public String userName;
    public String email;
    public float balance;
    public float expense;
    public float income;

    public Saving() {
    }

    public Saving(String name, String userName, String email, float balance, float expense, String s, float income) {
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.balance = balance;
        this.expense = expense;
        this.income = income;
    }


}
