package com.blackrock.utils;

public class TaxCalculator {

    public static double calculateTax(double income) {
        if (income <= 700000) {
            return 0;
        }

        if (income <= 1000000) {
            return (income - 700000) * 0.10;
        }

        if (income <= 1200000) {
            return (300000 * 0.10) + ((income - 1000000) * 0.15);
        }

        if (income <= 1500000) {
            return (300000 * 0.10) + (200000 * 0.15) +
                    ((income - 1200000) * 0.20);
        }

        // above 15L
        return (300000 * 0.10) + (200000 * 0.15) + (300000 * 0.20) +
                ((income - 1500000) * 0.30);
    }

    public static double computeTaxBenefit(double income, double deduction) {
        if (deduction <= 0) return 0;

        double taxBefore = calculateTax(income);
        double taxAfter = calculateTax(income - deduction);

        return taxBefore - taxAfter;
    }
}