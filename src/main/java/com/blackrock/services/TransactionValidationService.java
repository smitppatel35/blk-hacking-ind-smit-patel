package com.blackrock.services;

import com.blackrock.dto.InvalidProperty;
import com.blackrock.dto.InvalidTransaction;
import com.blackrock.dto.TransactionResponse;
import com.blackrock.dto.ValidationResult;
import com.blackrock.models.Transaction;
import com.blackrock.utils.CeilingUtils;
import com.blackrock.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TransactionValidationService {

    private static final double MAX_AMOUNT = 500000; // 5 × 10^5

    public ValidationResult validate(double wage, List<Transaction> transactions) {

        List<Transaction> valid = new ArrayList<>();
        List<InvalidProperty> invalid = new ArrayList<>();

        if (wage < 0) {
            invalid.add(new InvalidProperty(wage, "Wage should not be less than 0."));
        }

        validateTransactions(transactions, invalid, valid, wage);

        ValidationResult result = new ValidationResult();
        result.setValid(valid);
        result.setInvalid(invalid);
        return result;
    }

    public void validateTransactions(List<Transaction> transactions, List<InvalidProperty> invalid, List<Transaction> valid, double wage) {
        Set<String> seenDates = new HashSet<>();

        for (Transaction t : transactions) {
            double amount = t.getAmount();
            double ceiling = t.getCeiling();
            double remanent = t.getRemanent();
            String dateStr;

            try {
                dateStr = DateUtils.format(t.getDate());
            } catch (Exception e) {
                invalid.add(new InvalidTransaction(null, amount, ceiling, remanent, "Invalid or null dates are not allowed.", wage));
                continue;
            }

            // Duplicate detection
            if (seenDates.contains(dateStr)) {
                invalid.add(new InvalidTransaction(dateStr, amount, ceiling, remanent, "Duplicate transactions are not allowed.", wage));
                continue;
            }

            seenDates.add(dateStr);

            // Invalid amount < 0
            if (amount <= 0) {
                invalid.add(new InvalidTransaction(dateStr, amount, ceiling, remanent, "Negative amounts are not allowed", wage));
                continue;
            }

            // Invalid amount >= 500,000 (5 x 10^5)
            if (amount >= MAX_AMOUNT) {
                invalid.add(new InvalidTransaction(dateStr, amount, ceiling, remanent, "Transaction Amount more than 500000 are not allowed", wage));
                continue;
            }

            // ceiling must be valid
            double expectedCeiling = CeilingUtils.roundToNext100(amount);
            if (ceiling != expectedCeiling) {
                invalid.add(new InvalidTransaction(dateStr, t.getAmount(), t.getCeiling(), t.getRemanent(), "Ceiling value is incorrect", wage));
                continue;
            }

            double expectedRem = expectedCeiling - t.getAmount();
            if (t.getRemanent() != expectedRem) {
                invalid.add(new InvalidTransaction(dateStr, t.getAmount(), t.getCeiling(), t.getRemanent(), "Remanent value is incorrect", wage));
                continue;
            }

            if (expectedRem < 0) {
                invalid.add(new InvalidTransaction(dateStr, t.getAmount(), t.getCeiling(), t.getRemanent(), "Remanent cannot be negative", wage));
                continue;
            }

            // Valid
            valid.add(new Transaction(DateUtils.parse(dateStr), amount, ceiling, remanent));
        }
    }
}