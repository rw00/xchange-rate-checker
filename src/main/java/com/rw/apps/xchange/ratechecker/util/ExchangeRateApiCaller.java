package com.rw.apps.xchange.ratechecker.util;

import com.rw.apps.xchange.ratechecker.model.ExchangeRate;
import com.rw.apps.xchange.ratechecker.provider.ExchangeRateProvider;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class ExchangeRateApiCaller {
    public ExchangeRate call(ExchangeRateProvider exchangeRateProvider) {
        return exchangeRateProvider.getEurToUsdExchangeRate();
    }
}
