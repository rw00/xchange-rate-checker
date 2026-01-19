package com.rw.apps.xchange.ratechecker.provider.paysend.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.rw.apps.xchange.ratechecker.provider.ExchangeRateProvider;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EffectiveRateCalculator {
    private static final BigDecimal ONE_THOUSAND = BigDecimal.valueOf(1000);
    private static final BigDecimal MIN_PAY_SEND_FEES = BigDecimal.valueOf(1.5);
    private static final BigDecimal PAY_SEND_FEES_MULTIPLIER = BigDecimal.valueOf(0.01);
    private static final BigDecimal RECEIVING_FEES_MULTIPLIER = BigDecimal.valueOf(0.005);

    public String calculateEffectiveRate(BigDecimal exchangeRate) {
        BigDecimal sendingFees = calculatePaySendFees(ONE_THOUSAND);
        BigDecimal receivingFees = calculateReceivingFees(ONE_THOUSAND);

        // account for receiving fees
        BigDecimal effectiveReceivedValue = ONE_THOUSAND.add(receivingFees)
                .divide(exchangeRate, ExchangeRateProvider.PRECISION, RoundingMode.HALF_UP)
                .add(sendingFees);

        BigDecimal effectiveRate = BigDecimal.ONE
                .divide(effectiveReceivedValue, 7, RoundingMode.HALF_UP).multiply(ONE_THOUSAND);
        return effectiveRate.toString();
    }

    private BigDecimal calculatePaySendFees(BigDecimal amount) {
        return amount.multiply(PAY_SEND_FEES_MULTIPLIER).max(MIN_PAY_SEND_FEES);
    }

    private BigDecimal calculateReceivingFees(BigDecimal amount) {
        return amount.multiply(RECEIVING_FEES_MULTIPLIER);
    }
}
