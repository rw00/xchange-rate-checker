package com.rw.apps.xchange.ratechecker.provider.taptapsend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ToCountryFxRate(@JsonProperty("isoCountryCode") String country,
                              @JsonProperty("currency") String currency,
                              @JsonProperty("fxRate") String rate) {
}
