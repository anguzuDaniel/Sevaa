package com.danotech.sevaa.helpers;

public enum ExpenseType {
    SUBSCRIPTION, ONE_TIME_PAYMENT, UNKNOWN;

    public static ExpenseType getExpenseTypeById(long id) {
        switch ((int) id) {
            case 1:
                return ExpenseType.SUBSCRIPTION;
            case 2:
                return ExpenseType.ONE_TIME_PAYMENT;
            default:
                return ExpenseType.UNKNOWN;
        }
    }
}
