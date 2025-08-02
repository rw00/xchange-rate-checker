package com.rw.apps.xchange.ratechecker.util;

import com.rw.apps.xchange.ratechecker.model.ExchangeRate;
import com.rw.apps.xchange.ratechecker.provider.paysend.PaySendApi;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class ExchangeRateApiCallerRealTest {
    @org.junit.jupiter.api.Disabled
    @Test
    void basicTest() {
        ExchangeRate exchangeRate = ExchangeRateApiCaller.call(new PaySendApi());
        log.info(exchangeRate.toString());
    }
}
