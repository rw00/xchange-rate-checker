package com.rw.apps.xchange.ratechecker.provider.whish;

import com.rw.apps.xchange.ratechecker.model.Currency;
import com.rw.apps.xchange.ratechecker.model.ExchangeRate;
import com.rw.apps.xchange.ratechecker.provider.ExchangeRateProvider;
import org.springframework.stereotype.Component;

@Component
public class WhishMoneyProvider implements ExchangeRateProvider {
    @Override
    public ExchangeRate getEurToUsdExchangeRate() {
        // 1% flat rate
        return new ExchangeRate(Currency.US_DOLLARS.getCode(), Currency.US_DOLLARS.getCode(), "1.01");
    }
}
