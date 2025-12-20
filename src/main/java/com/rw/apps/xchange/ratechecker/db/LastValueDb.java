package com.rw.apps.xchange.ratechecker.db;

import com.rw.apps.xchange.ratechecker.util.NumberUtils;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.stereotype.Component;

@Component
public class LastValueDb {
    private static final double GAP_THRESHOLD = 0.01;
    private final AtomicReference<LastValuesHolder> lastValuesHolderRef = new AtomicReference<>();

    public boolean updateIfGreater(ExchangeRateComparison currentRates) {
        LastValuesHolder lastValuesHolder = lastValuesHolderRef.get();

        boolean anyRateLowDiff = currentRates.providerRates().values().stream()
                .anyMatch(rate -> NumberUtils.gt("1.001", rate));

        if (anyRateLowDiff) {
            lastValuesHolderRef.set(new LastValuesHolder(
                    lastValuesHolder == null ? null : lastValuesHolder.lastRecord(), currentRates));
            return false;
        }

        if (lastValuesHolder == null || isRecord(currentRates, lastValuesHolder)) {
            // set new record
            lastValuesHolderRef.set(new LastValuesHolder(currentRates, currentRates));
            return true;
        }
        // open rate decreased,
        // but app rate increased since last check; update last value
        if (hasIncreased(currentRates, lastValuesHolder)) {
            lastValuesHolderRef.set(new LastValuesHolder(lastValuesHolder.lastRecord(), currentRates));
            return true;
        }

        // check if any provider has a small gap with open rate
        boolean anySmallGap = currentRates.providerRates().values().stream()
                .anyMatch(rate -> Math.abs(NumberUtils.asDecimal(rate)
                        .subtract(NumberUtils.asDecimal(currentRates.openRate()))
                        .doubleValue()) < GAP_THRESHOLD);
        if (anySmallGap) {
            lastValuesHolderRef.set(new LastValuesHolder(lastValuesHolder.lastRecord(), currentRates));
            return true;
        }

        // keep last record but update last value
        lastValuesHolderRef.set(new LastValuesHolder(lastValuesHolder.lastRecord(), currentRates));
        return false;
    }

    private boolean isRecord(ExchangeRateComparison currentRates, LastValuesHolder lastValuesHolder) {
        if (lastValuesHolder.lastRecord() == null)
            return true;
        return currentRates.providerRates().entrySet().stream()
                .anyMatch(entry -> {
                    String lastRecordRate = lastValuesHolder.lastRecord().providerRates().get(entry.getKey());
                    return lastRecordRate != null && NumberUtils.gt(entry.getValue(), lastRecordRate);
                });
    }

    private boolean hasIncreased(ExchangeRateComparison currentRates, LastValuesHolder lastValuesHolder) {
        if (lastValuesHolder.lastValue() == null)
            return true;
        return currentRates.providerRates().entrySet().stream()
                .anyMatch(entry -> {
                    String lastValueRate = lastValuesHolder.lastValue().providerRates().get(entry.getKey());
                    return lastValueRate != null && NumberUtils.gt(entry.getValue(), lastValueRate);
                });
    }
}
