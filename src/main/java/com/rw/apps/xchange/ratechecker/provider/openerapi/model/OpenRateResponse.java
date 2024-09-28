package com.rw.apps.xchange.ratechecker.provider.openerapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public record OpenRateResponse(@JsonProperty("base_code") String baseCurrencyCode,
                               @JsonProperty("rates") Map<String, Double> rates) {
}
