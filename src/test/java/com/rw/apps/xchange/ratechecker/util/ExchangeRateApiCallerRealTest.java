package com.rw.apps.xchange.ratechecker.util;

import com.rw.apps.xchange.ratechecker.model.ExchangeRate;
import com.rw.apps.xchange.ratechecker.provider.whish.remitly.RemitlyWhishProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Slf4j
class ExchangeRateApiCallerRealTest {
    @Disabled
    @Test
    void basicTest() {
        ExchangeRate exchangeRate = ExchangeRateApiCaller.call(new RemitlyWhishProvider());
        log.info(exchangeRate.toString());
    }
}
