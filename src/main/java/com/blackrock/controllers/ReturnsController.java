package com.blackrock.controllers;

import com.blackrock.dto.FilterRequest;
import com.blackrock.dto.ReturnsRequest;
import com.blackrock.dto.ReturnsResponse;
import com.blackrock.dto.ValidationResult;
import com.blackrock.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blackrock/challenge/v1")
public class ReturnsController {

    @Autowired
    private TransactionValidationService validationService;

    @Autowired
    private TemporalFilterService temporalService;

    @Autowired
    private ReturnsService returnsService;

    // -----------------------------------------------------------------------------------------
    // NPS RETURNS
    // -----------------------------------------------------------------------------------------
    @PostMapping("returns:nps")
    public Object calcNps(
            @RequestBody ReturnsRequest req
    ) {

        ValidationResult validate = validationService.validate(req.getWage(), req.getTransactions());

        if (!validate.getInvalid().isEmpty()) {
            return validate;
        }

        var updated = temporalService.applyQandP(validate.getValid(), req.getQ(), req.getP());
        var grouped = temporalService.groupByK(updated, req.getK());

        return returnsService.calculateNps(req.getAge(), req.getWage(), req.getInflation(), req.getK(), grouped);
    }

    // -----------------------------------------------------------------------------------------
    // INDEX RETURNS
    // -----------------------------------------------------------------------------------------
    @PostMapping("returns:index")
    public Object calcIndex(
            @RequestBody ReturnsRequest req
    ) {

        ValidationResult validate = validationService.validate(req.getWage(), req.getTransactions());

        if (!validate.getInvalid().isEmpty()) {
            return validate;
        }

        var updated = temporalService.applyQandP(validate.getValid(), req.getQ(), req.getP());
        var grouped = temporalService.groupByK(updated, req.getK());

        return returnsService.calculateIndex(req.getAge(), req.getWage(), req.getInflation(), req.getK(), grouped);
    }
}