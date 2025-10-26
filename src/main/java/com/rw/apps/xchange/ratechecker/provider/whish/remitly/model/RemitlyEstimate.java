package com.rw.apps.xchange.ratechecker.provider.whish.remitly.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RemitlyEstimate(@JsonProperty("exchange_rate") RemitlyExchangeRate exchangeRate) {
}
