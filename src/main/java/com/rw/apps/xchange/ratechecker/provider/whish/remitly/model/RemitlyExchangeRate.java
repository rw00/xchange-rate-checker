package com.rw.apps.xchange.ratechecker.provider.whish.remitly.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RemitlyExchangeRate(@JsonProperty("base_rate") String baseRate) {
}
