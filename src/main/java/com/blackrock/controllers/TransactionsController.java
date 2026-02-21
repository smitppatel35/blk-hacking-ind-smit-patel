package com.blackrock.controllers;

import com.blackrock.dto.*;
import com.blackrock.services.TemporalFilterService;
import com.blackrock.services.TransactionParseService;
import com.blackrock.services.TransactionValidationService;
import com.blackrock.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/blackrock/challenge/v1")
public class TransactionsController {

    @Autowired
    private TransactionParseService parseService;

    @Autowired
    private TransactionValidationService validationService;

    @Autowired
    private TemporalFilterService temporalService;

    // -----------------------------------------------------------------------------------------
    // 1) PARSE ENDPOINT
    // -----------------------------------------------------------------------------------------
    @PostMapping("transactions:parse")
    public List<TransactionResponse> parse(@RequestBody List<TransactionRequest> req) {
        var transactions = parseService.parse(req);

        return transactions.stream()
                .map(t -> new TransactionResponse(
                        DateUtils.format(t.getDate()),
                        t.getAmount(),
                        t.getCeiling(),
                        t.getRemanent()))
                .toList();
    }

    // -----------------------------------------------------------------------------------------
    // 2) VALIDATOR ENDPOINT
    // -----------------------------------------------------------------------------------------
    @PostMapping("transactions:validator")
    public ValidationResult validate(@RequestBody ValidatorRequest req) {
        return validationService.validate(req.getWage(), req.getTransactions());
    }

    // -----------------------------------------------------------------------------------------
    // 3) FILTER ENDPOINT (Q / P / K rules)
    // -----------------------------------------------------------------------------------------
    @PostMapping("transactions:filter")
    public Object filter(@RequestBody FilterRequest req) {

        ValidationResult validate = validationService.validate(req.getWage(), req.getTransactions());

        if (!validate.getInvalid().isEmpty()) {
            return validate;
        }

        // 2. apply Q+P rules
        var updatedTx = temporalService.applyQandP(validate.getValid(), req.getQ(), req.getP());

        // 3. GROUP BY K
        var grouped = temporalService.groupByK(updatedTx, req.getK());

        // Prepare simple output
        return grouped.entrySet().stream().map(entry -> {
            var k = entry.getKey();
            var amount = entry.getValue();

            return new SavingsByDates(
                    DateUtils.format(k.getStart()),
                    DateUtils.format(k.getEnd()),
                    amount,
                    0,
                    0
            );
        }).toList();

    }
}