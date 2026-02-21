package com.blackrock;

import com.blackrock.models.*;
import com.blackrock.dto.TransactionRequest;
import com.blackrock.services.*;
import com.blackrock.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QPeriodTest {

    private final TransactionParseService parse = new TransactionParseService();
    private final TemporalFilterService service = new TemporalFilterService();

    @Test
    public void testQOverride() {

        // Transaction on July 10
        TransactionRequest req = new TransactionRequest();
        req.setDate("2023-07-10 12:00:00");
        req.setAmount(250);

        var tx = parse.parse(List.of(req));

        // Q1 (older)
        PeriodQ q1 = new PeriodQ();
        q1.setFixed(0);
        q1.setStart(DateUtils.parse("2023-07-01 00:00:00"));
        q1.setEnd(DateUtils.parse("2023-07-31 23:59:59"));

        // Q2 (later — should win)
        PeriodQ q2 = new PeriodQ();
        q2.setFixed(100);
        q2.setStart(DateUtils.parse("2023-07-05 00:00:00"));
        q2.setEnd(DateUtils.parse("2023-07-20 23:59:59"));

        List<PeriodQ> plist = new ArrayList<>();
        plist.add(q1);
        plist.add(q2);

        var updated = service.applyQandP(tx, plist, Collections.emptyList());

        assertEquals(100, updated.get(0).getRemanent());
    }
}