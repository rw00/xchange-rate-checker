package com.rw.apps.xchange.ratechecker.db;

import java.time.Instant;
import java.util.Map;

public record ExchangeRateComparison(
                String openRate,
                Map<String, String> providerRates,
                Instant timestamp) {
}
