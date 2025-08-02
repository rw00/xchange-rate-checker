package com.rw.apps.xchange.ratechecker.provider.wisewhish.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EffectiveRateCalculator {
    private static final BigDecimal BASE_VALUE = BigDecimal.valueOf(1000);
    private static final BigDecimal WHISH_FEES_UNMULTIPLIER = BigDecimal.valueOf(0.99);

    public String calculateEffectiveRate(BigDecimal exchangeRate) {
        BigDecimal exchangedValue = BASE_VALUE.multiply(exchangeRate);
        BigDecimal cashedOutValue = exchangedValue.multiply(WHISH_FEES_UNMULTIPLIER);
        BigDecimal effectiveExchangeRate = cashedOutValue.divide(BASE_VALUE, 4, RoundingMode.HALF_UP);
        return effectiveExchangeRate.toString();
    }
}
