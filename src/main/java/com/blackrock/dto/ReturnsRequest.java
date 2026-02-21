package com.blackrock.dto;

import com.blackrock.models.PeriodK;
import com.blackrock.models.PeriodP;
import com.blackrock.models.PeriodQ;
import com.blackrock.models.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnsRequest {
    private Integer age;
    private Float inflation;
    private Double wage;
    private List<Transaction> transactions;
    private List<PeriodQ> q;
    private List<PeriodP> p;
    private List<PeriodK> k;
}
