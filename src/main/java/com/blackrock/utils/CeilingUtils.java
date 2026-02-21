package com.blackrock.utils;

public class CeilingUtils {

    public static double roundToNext100(double amount) {
        if (amount <= 0) return 0;

        double remainder = amount % 100;
        if (remainder == 0) return amount + 100;

        return amount + (100 - remainder);
    }

    public static double remanent(double amount, double ceiling) {
        return ceiling - amount;
    }
}