package com.blackrock.services;

import com.blackrock.models.PeriodK;
import com.blackrock.dto.SavingsByDates;
import com.blackrock.dto.ReturnsResponse;
import com.blackrock.utils.DateUtils;
import com.blackrock.utils.InterestUtils;
import com.blackrock.utils.TaxCalculator;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReturnsService {

    public ReturnsResponse calculateNps(
            int age,
            double wage,
            double inflation,
            List<PeriodK> kList,
            Map<PeriodK, Double> kAmounts
    ) {

        int years = Math.max(60 - age, 5); // PDF rule

        List<SavingsByDates> savings = new ArrayList<>();

        double totalAmount = 0;
        double totalCeiling = 0;

        for (PeriodK k : kList) {
            double principal = kAmounts.get(k);
            totalAmount += principal;

            double finalAmount = InterestUtils.calculateNpsFinal(principal, years);

            double realValue = InterestUtils.inflationAdjust(finalAmount, inflation/100, years);

            double npsLimit = Math.min(principal, Math.min(wage * 0.10, 200000));
            double taxBenefit = TaxCalculator.computeTaxBenefit(wage, npsLimit);

            savings.add(new SavingsByDates(
                    DateUtils.format(k.getStart()),
                    DateUtils.format(k.getEnd()),
                    principal,
                    Math.round(realValue),
                    Math.round(taxBenefit)
            ));
        }



        ReturnsResponse resp = new ReturnsResponse();
        resp.setTransactionsTotalAmount(totalAmount);
        resp.setTransactionsTotalCeiling(totalCeiling);
        resp.setSavingsByDates(savings);

        return resp;
    }

    // --------------------------------------------
    // Index fund returns
    // --------------------------------------------

    public ReturnsResponse calculateIndex(
            int age,
            double wage,
            double inflation,
            List<PeriodK> kList,
            Map<PeriodK, Double> kAmounts
    ) {

        int years = Math.max(60 - age, 5);

        List<SavingsByDates> savings = new ArrayList<>();
        double totalAmount = 0;

        for (PeriodK k : kList) {
            double principal = kAmounts.get(k);
            totalAmount += principal;

            double finalAmount = InterestUtils.calculateIndexFinal(principal, years);
            double realValue = InterestUtils.inflationAdjust(finalAmount, inflation/100, years);

            savings.add(new SavingsByDates(
                    DateUtils.format(k.getStart()),
                    DateUtils.format(k.getEnd()),
                    principal,
                    Math.round(realValue),
                    0 // index = no tax benefit
            ));
        }

        ReturnsResponse resp = new ReturnsResponse();
        resp.setTransactionsTotalAmount(totalAmount);
        resp.setSavingsByDates(savings);
        return resp;
    }
}