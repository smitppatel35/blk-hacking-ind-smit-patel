package com.blackrock.services;

import com.blackrock.dto.TransactionRequest;
import com.blackrock.services.TransactionParseService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionParseServiceTest {

    private final TransactionParseService service = new TransactionParseService();

    @Test
    public void testParse() {
        TransactionRequest req = new TransactionRequest();
        req.setDate("2023-10-12 20:15:00");
        req.setAmount(250);

        var list = service.parse(List.of(req));
        var tx = list.get(0);

        assertEquals(300, tx.getCeiling());
        assertEquals(50, tx.getRemanent());
    }
}