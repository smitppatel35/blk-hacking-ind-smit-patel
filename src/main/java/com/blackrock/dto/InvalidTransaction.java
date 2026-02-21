package com.blackrock.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InvalidTransaction extends InvalidProperty {
    private String date;
    private double amount;
    private double ceiling;
    private double remanent;

    public InvalidTransaction(String date, double amount, double ceiling, double remanent, String message, double wage) {
        super(wage, message);
        this.date = date;
        this.amount = amount;
        this.ceiling = ceiling;
        this.remanent = remanent;
    }
}