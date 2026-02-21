package com.blackrock.dto;

import com.blackrock.models.PeriodK;
import com.blackrock.models.PeriodP;
import com.blackrock.models.PeriodQ;
import com.blackrock.models.Transaction;
import lombok.Data;

import java.util.List;

@Data
public class FilterRequest {
    private double wage;
    private List<Transaction> transactions;
    private List<PeriodQ> q;
    private List<PeriodP> p;
    private List<PeriodK> k;
}