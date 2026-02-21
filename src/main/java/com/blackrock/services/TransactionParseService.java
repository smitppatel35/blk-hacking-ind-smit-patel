package com.blackrock.services;

import com.blackrock.dto.TransactionRequest;
import com.blackrock.models.Transaction;
import com.blackrock.utils.CeilingUtils;
import com.blackrock.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionParseService {

    public List<Transaction> parse(List<TransactionRequest> requests) {
        return requests.stream().map(r -> {
            var date = DateUtils.parse(r.getDate());
            double amount = r.getAmount();

            double ceiling = CeilingUtils.roundToNext100(amount);
            double remanent = CeilingUtils.remanent(amount, ceiling);

            return new Transaction(date, amount, ceiling, remanent);
        }).collect(Collectors.toList());
    }
}