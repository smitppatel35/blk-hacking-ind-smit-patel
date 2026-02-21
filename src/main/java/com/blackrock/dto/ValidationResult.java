package com.blackrock.dto;

import com.blackrock.models.Transaction;
import lombok.Data;

import java.util.List;

@Data
public class ValidationResult {
    private List<Transaction> valid;
    private List<InvalidProperty> invalid;
}