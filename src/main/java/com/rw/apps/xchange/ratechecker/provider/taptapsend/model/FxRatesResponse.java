package com.rw.apps.xchange.ratechecker.provider.taptapsend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record FxRatesResponse(@JsonProperty("availableCountries") List<FromCountryFxRates> ratesByCountry) {

    public FromCountryFxRates getFxRatesForCountry(String country) {
        return ratesByCountry.stream()
                             .filter(e -> country.equals(e.country()))
                             .findFirst()
                             .orElseThrow();
    }
}
