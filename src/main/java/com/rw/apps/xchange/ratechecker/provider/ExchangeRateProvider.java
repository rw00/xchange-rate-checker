package com.rw.apps.xchange.ratechecker.provider;

import com.rw.apps.xchange.ratechecker.model.ExchangeRate;

public interface ExchangeRateProvider {
    ExchangeRate getEurToUsdExchangeRate();
}
