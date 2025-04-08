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
        if (NumberUtils.gt("1.001", currentRates.taptapsendRate())) {
            lastValuesHolderRef.set(new LastValuesHolder(lastValuesHolder.lastRecord(), currentRates));
            return false;
        }
        if (lastValuesHolder == null ||
            // set new record
            isRecord(currentRates, lastValuesHolder)) {
            lastValuesHolderRef.set(new LastValuesHolder(currentRates, currentRates));
            return true;
        }
        // open rate decreased, but app rate increased since last check; update last value
        if (hasIncreased(currentRates, lastValuesHolder)) {
            lastValuesHolderRef.set(new LastValuesHolder(lastValuesHolder.lastRecord(), currentRates));
            return true;
        }
        // small gap
        if (Math.abs(NumberUtils.asDecimal(currentRates.taptapsendRate())
                                .subtract(NumberUtils.asDecimal(currentRates.openRate()))
                                .doubleValue())
            < GAP_THRESHOLD) {
            lastValuesHolderRef.set(new LastValuesHolder(lastValuesHolder.lastRecord(), currentRates));
            return true;
        }
        // keep last record but update last value
        lastValuesHolderRef.set(new LastValuesHolder(lastValuesHolder.lastRecord(), currentRates));
        return false;
    }

    private boolean isRecord(ExchangeRateComparison currentRates, LastValuesHolder lastValuesHolder) {
        return NumberUtils.gt(currentRates.taptapsendRate(), lastValuesHolder.lastRecord().taptapsendRate());
    }

    private boolean hasIncreased(ExchangeRateComparison currentRates, LastValuesHolder lastValuesHolder) {
        return NumberUtils.gt(currentRates.taptapsendRate(), lastValuesHolder.lastValue().taptapsendRate());
    }
}
