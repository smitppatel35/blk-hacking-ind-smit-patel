package com.blackrock.dto;

import lombok.Data;

@Data
public class TransactionRequest {
    private String date;     // incoming strings
    private double amount;
}