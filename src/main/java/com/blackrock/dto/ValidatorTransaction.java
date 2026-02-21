package com.blackrock.dto;

import lombok.Data;

@Data
public class ValidatorTransaction {
    private String date;
    private double amount;
    private double ceiling;
    private double remanent;

    // Constructed after /parse endpoint processing
    public ValidatorTransaction(String date, double amount, double ceiling, double remanent) {
        this.date = date;
        this.amount = amount;
        this.ceiling = ceiling;
        this.remanent = remanent;
    }
}
