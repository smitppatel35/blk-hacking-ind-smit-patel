package com.blackrock.services;

import com.blackrock.dto.TransactionRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionValidationServiceTest {

    private final TransactionParseService parse = new TransactionParseService();
    private final TransactionValidationService val = new TransactionValidationService();

    @Test
    public void testValidation() {
        TransactionRequest r1 = new TransactionRequest();
        r1.setDate("2023-01-01 10:00:00");
        r1.setAmount(250);

        TransactionRequest r2 = new TransactionRequest();
        r2.setDate("2023-01-01 10:00:00");
        r2.setAmount(300);

        TransactionRequest r3 = new TransactionRequest();
        r3.setDate("2023-01-02 10:00:00");
        r3.setAmount(-1);

        var parsed = parse.parse(List.of(r1, r2, r3));
        var result = val.validate(50000, parsed);

        assertEquals(1, result.getValid().size());
        assertEquals(2, result.getInvalid().size());
    }
}