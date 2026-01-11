package com.rw.apps.xchange.ratechecker.provider.taptapsend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record FromCountryFxRates(@JsonProperty("isoCountryCode") String country,
        @JsonProperty("currency") String currency,
        @JsonProperty("corridors") List<ToCountryFxRate> rates) {
    public ToCountryFxRate getToCountryFxRate(String country) {
        return rates.stream()
                .filter(e -> country.equals(e.country()))
                .findFirst()
                .orElseThrow();
    }
}
