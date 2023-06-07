package com.danotech.sevaa.helpers;

public enum ExpenseType {
    SUBSCRIPTION(0),
    ONE_TIME_PAYMENT(1),
    UNKNOWN(2);

    private final int id;

    ExpenseType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ExpenseType getExpenseTypeById(int id) {
        switch (id) {
            case 0:
                return ExpenseType.SUBSCRIPTION;
            case 1:
                return ExpenseType.ONE_TIME_PAYMENT;
            default:
                return ExpenseType.UNKNOWN;
        }
    }
}
