package com.rw.apps.xchange.ratechecker.provider.whish.wise.util;

import com.rw.apps.xchange.ratechecker.provider.ExchangeRateProvider;
import com.rw.apps.xchange.ratechecker.provider.whish.FeeCalculator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EffectiveRateCalculator {
    private final BigDecimal CONVERSION_FEE = BigDecimal.valueOf(3.93); // €3.93 conversion fee for $1000 exchanged

    public String calculateEffectiveRate(BigDecimal exchangeRate) {
        BigDecimal receivedAmount = ExchangeRateProvider.BASE_VALUE.multiply(exchangeRate);

        BigDecimal amountAfterWiseFee = receivedAmount.subtract(CONVERSION_FEE);

        BigDecimal amountAfterWhishFee = FeeCalculator.applyWhishFee(amountAfterWiseFee);

        BigDecimal effectiveExchangeRate = amountAfterWhishFee.divide(ExchangeRateProvider.BASE_VALUE,
                ExchangeRateProvider.PRECISION, RoundingMode.HALF_UP);
        return effectiveExchangeRate.toString();
    }
}
