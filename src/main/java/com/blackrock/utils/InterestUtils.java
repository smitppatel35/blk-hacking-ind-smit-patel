package com.blackrock.utils;

public class InterestUtils {

    public static double compound(double principal, double rate, int years) {
        return principal * Math.pow(1 + rate, years);
    }

    public static double inflationAdjust(double amount, double inflationRate, int years) {
        return amount / Math.pow(1 + inflationRate, years);
    }

    public static double calculateNpsFinal(double principal, int years) {
        return compound(principal, 0.0711, years);
    }

    public static double calculateIndexFinal(double principal, int years) {
        return compound(principal, 0.1449, years);
    }
}