package com.rw.apps.xchange.ratechecker.provider.open.exchangerate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public record OpenRateResponse(@JsonProperty("base") String baseCurrencyCode,
                               @JsonProperty("rates") Map<String, Double> rates) {
}
