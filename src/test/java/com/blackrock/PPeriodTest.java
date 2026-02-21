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

public class PPeriodTest {

    private final TransactionParseService parse = new TransactionParseService();
    private final TemporalFilterService service = new TemporalFilterService();

    @Test
    public void testPAdditive() {
        TransactionRequest req = new TransactionRequest();
        req.setDate("2023-10-10 12:00:00");
        req.setAmount(250);

        var tx = parse.parse(List.of(req));

        PeriodP p1 = new PeriodP();
        p1.setExtra(20);
        p1.setStart(DateUtils.parse("2023-10-01 00:00:00"));
        p1.setEnd(DateUtils.parse("2023-10-31 23:59:00"));

        PeriodP p2 = new PeriodP();
        p2.setExtra(30);
        p2.setStart(DateUtils.parse("2023-10-05 00:00:00"));
        p2.setEnd(DateUtils.parse("2023-10-15 23:59:00"));

        List<PeriodP> plist = new ArrayList<>();
        plist.add(p1);
        plist.add(p2);

        var updated = service.applyQandP(tx, Collections.emptyList(), plist);

        // Remanent originally = 50 → plus 20 + 30 = 100
        assertEquals(100, updated.get(0).getRemanent());
    }
}