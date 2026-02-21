package com.blackrock.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReturnsResponse {
    private double transactionsTotalAmount;
    private double transactionsTotalCeiling;
    private List<SavingsByDates> savingsByDates;
}