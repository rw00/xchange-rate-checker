package com.rw.apps.xchange.ratechecker.db;

import java.time.Instant;

public record ExchangeRateComparison(
        String openRate,
        String taptapsendRate,
        String remitlyWhishRate,
        String wiseWhishRate,
        Instant timestamp) {
}
