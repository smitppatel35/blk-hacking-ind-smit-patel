package com.blackrock;

import com.blackrock.dto.TransactionRequest;
import com.blackrock.models.*;
import com.blackrock.utils.DateUtils;
import com.blackrock.services.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KGroupingTest {

    private final TransactionParseService parse = new TransactionParseService();
    private final TemporalFilterService service = new TemporalFilterService();

    @Test
    public void testKGrouping() {

        TransactionRequest r1 = new TransactionRequest();
        r1.setDate("2023-03-10 10:00:00");
        r1.setAmount(250); // remanent = 50

        TransactionRequest r2 = new TransactionRequest();
        r2.setDate("2023-05-10 10:00:00");
        r2.setAmount(375); // remanent = 25

        var tx = parse.parse(List.of(r1, r2));

        PeriodK k = new PeriodK();
        k.setStart(DateUtils.parse("2023-03-01 00:00:00"));
        k.setEnd(DateUtils.parse("2023-11-30 23:59:00"));

        var grouped = service.groupByK(tx, List.of(k));

        assertEquals(75, grouped.get(k));
    }
}