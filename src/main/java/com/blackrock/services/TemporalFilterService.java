package com.blackrock.services;

import com.blackrock.models.PeriodK;
import com.blackrock.models.PeriodP;
import com.blackrock.models.PeriodQ;
import com.blackrock.models.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TemporalFilterService {

    public List<Transaction> applyQandP(
            List<Transaction> transactions,
            List<PeriodQ> qList,
            List<PeriodP> pList
    ) {

        // Sort Q periods by start ascending
        qList.sort(Comparator.comparing(PeriodQ::getStart));

        // Sort P periods by start ascending
        pList.sort(Comparator.comparing(PeriodP::getStart));

        for (Transaction t : transactions) {
            LocalDateTime date = t.getDate();

            // ---------------------------
            // APPLY Q OVERRIDE (fixed)
            // ---------------------------
            double overridden = t.getRemanent();
            PeriodQ bestQ = null;

            // Binary-search-like backward scan
            for (PeriodQ q : qList) {
                if (q.getStart().isAfter(date)) break;
                if (!q.getEnd().isBefore(date)) {
                    if (bestQ == null || q.getStart().isAfter(bestQ.getStart())) {
                        bestQ = q;
                    }
                }
            }

            if (bestQ != null) {
                overridden = bestQ.getFixed();
            }

            // ---------------------------
            // APPLY P ADDITIONS
            // ---------------------------
            double extraAdd = 0;

            for (PeriodP p : pList) {
                if (p.getStart().isAfter(date)) break;
                if (!p.getEnd().isBefore(date)) {
                    extraAdd += p.getExtra();
                }
            }

            t.setRemanent(overridden + extraAdd);
        }

        return transactions;
    }

    // -----------------------------------------------------------------------------
    // K-GROUPING: sum remanent by each k period
    // -----------------------------------------------------------------------------

    public Map<PeriodK, Double> groupByK(
            List<Transaction> transactions,
            List<PeriodK> kList
    ) {
        Map<PeriodK, Double> result = new HashMap<>();

        for (PeriodK k : kList) {
            result.put(k, 0.0);
        }

        for (Transaction t : transactions) {
            LocalDateTime date = t.getDate();

            for (PeriodK k : kList) {
                if ((date.isEqual(k.getStart()) || date.isAfter(k.getStart()))
                        && (date.isEqual(k.getEnd()) || date.isBefore(k.getEnd()))) {

                    double updated = result.get(k) + t.getRemanent();
                    result.put(k, updated);
                }
            }
        }

        return result;
    }
}