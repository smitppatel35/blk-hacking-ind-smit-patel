package com.blackrock.dto;

import lombok.Data;

@Data
public class SavingsByDates {
    private String start;
    private String end;
    private double amount;
    private double profit;
    private double taxBenefit;

    public SavingsByDates(String start, String end, double amount, double profit, double taxBenefit) {
        this.start = start;
        this.end = end;
        this.amount = amount;
        this.profit = profit;
        this.taxBenefit = taxBenefit;
    }
}