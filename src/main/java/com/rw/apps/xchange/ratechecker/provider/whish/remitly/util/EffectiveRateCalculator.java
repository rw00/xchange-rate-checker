package com.rw.apps.xchange.ratechecker.provider.whish.remitly.util;

import com.rw.apps.xchange.ratechecker.provider.ExchangeRateProvider;
import com.rw.apps.xchange.ratechecker.provider.whish.FeeCalculator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EffectiveRateCalculator {
    private final BigDecimal FLAT_FEE = BigDecimal.valueOf(1.49);

    public String calculateEffectiveRate(BigDecimal exchangeRate) {
        BigDecimal amountAfterFlatFee = ExchangeRateProvider.BASE_VALUE.subtract(FLAT_FEE);
        BigDecimal receivedAmount = amountAfterFlatFee.multiply(exchangeRate);

        BigDecimal amountAfterWhishFee = FeeCalculator.applyWhishFee(receivedAmount);

        BigDecimal effectiveExchangeRate = amountAfterWhishFee.divide(ExchangeRateProvider.BASE_VALUE, ExchangeRateProvider.PRECISION, RoundingMode.HALF_UP);
        return effectiveExchangeRate.toString();
    }
}
