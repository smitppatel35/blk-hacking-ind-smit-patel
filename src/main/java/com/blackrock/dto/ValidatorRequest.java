package com.blackrock.dto;

import com.blackrock.models.Transaction;
import lombok.Data;

import java.util.List;

@Data
public class ValidatorRequest {
    private double wage;
    private List<Transaction> transactions;
}
