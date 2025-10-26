package com.rw.apps.xchange.ratechecker.provider.whish.wise.util;

import com.rw.apps.xchange.ratechecker.provider.whish.FeeCalculator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EffectiveRateCalculator {
    private final BigDecimal BASE_VALUE = BigDecimal.valueOf(1000);

    public String calculateEffectiveRate(BigDecimal exchangeRate) {
        BigDecimal receivedAmount = BASE_VALUE.multiply(exchangeRate);

        BigDecimal amountAfterWhishFee = FeeCalculator.applyWhishFee(receivedAmount);

        BigDecimal effectiveExchangeRate = amountAfterWhishFee.divide(BASE_VALUE, 4, RoundingMode.HALF_UP);
        return effectiveExchangeRate.toString();
    }
}
