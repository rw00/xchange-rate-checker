package com.rw.apps.xchange.ratechecker.provider;

import java.math.BigDecimal;

import com.rw.apps.xchange.ratechecker.model.ExchangeRate;

public interface ExchangeRateProvider {
    BigDecimal BASE_VALUE = BigDecimal.valueOf(1000);
    int PRECISION = 4;

    String getName();

    int getOrder();

    ExchangeRate getEurToUsdExchangeRate();
}
