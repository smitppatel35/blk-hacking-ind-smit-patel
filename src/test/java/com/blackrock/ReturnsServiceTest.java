package com.blackrock;

import com.blackrock.models.*;
import com.blackrock.utils.DateUtils;
import com.blackrock.services.ReturnsService;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReturnsServiceTest {

    private final ReturnsService returns = new ReturnsService();

    @Test
    public void testNpsReturns() {

        PeriodK k = new PeriodK();
        k.setStart(DateUtils.parse("2023-01-01 00:00:00"));
        k.setEnd(DateUtils.parse("2023-12-31 23:59:00"));

        Map<PeriodK, Double> map = Map.of(k, 145.0);

        var resp = returns.calculateNps(
                29,              // age
                600000,          // wage
                0.055,           // inflation
                List.of(k),
                map
        );

        assertEquals(145, resp.getTransactionsTotalAmount());
        assertTrue(resp.getSavingsByDates().get(0).getProfit() > 0);
    }
}