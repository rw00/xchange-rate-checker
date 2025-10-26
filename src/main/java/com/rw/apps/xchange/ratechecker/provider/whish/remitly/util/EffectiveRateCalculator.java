package com.rw.apps.xchange.ratechecker.provider.whish.remitly.util;

import com.rw.apps.xchange.ratechecker.provider.whish.FeeCalculator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EffectiveRateCalculator {
    private final BigDecimal BASE_VALUE = BigDecimal.valueOf(1000);
    private final BigDecimal FLAT_FEE = BigDecimal.valueOf(1.49);

    public String calculateEffectiveRate(BigDecimal exchangeRate) {
        BigDecimal amountAfterFlatFee = BASE_VALUE.subtract(FLAT_FEE);
        BigDecimal receivedAmount = amountAfterFlatFee.multiply(exchangeRate);

        BigDecimal amountAfterWhishFee = FeeCalculator.applyWhishFee(receivedAmount);

        BigDecimal effectiveExchangeRate = amountAfterWhishFee.divide(BASE_VALUE, 4, RoundingMode.HALF_UP);
        return effectiveExchangeRate.toString();
    }
}
