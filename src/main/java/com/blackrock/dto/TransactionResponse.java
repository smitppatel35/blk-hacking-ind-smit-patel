package com.blackrock.dto;

import lombok.Data;

@Data
public class TransactionResponse {
    private String date;
    private double amount;
    private double ceiling;
    private double remanent;

    public TransactionResponse(String date, double amount, double ceiling, double remanent) {
        this.date = date;
        this.amount = amount;
        this.ceiling = ceiling;
        this.remanent = remanent;
    }
}