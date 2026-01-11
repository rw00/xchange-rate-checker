package com.rw.apps.xchange.ratechecker.db;

import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExchangeRateComparison(
        @JsonProperty(required = true) String openRate,
        @JsonProperty(required = true) Map<String, String> providerRates,
        @JsonProperty(required = true) Instant timestamp) {
}
