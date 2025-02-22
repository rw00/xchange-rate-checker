package com.rw.apps.xchange.ratechecker.provider.paysend.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EffectiveRateCalculator {
    public String calculateEffectiveRate(BigDecimal exchangeRate) {
        BigDecimal oneThousand = BigDecimal.valueOf(1000);
        BigDecimal sendingFees = calculatePaySendFees(oneThousand);
        BigDecimal receivingFees = calculateReceivingFees(oneThousand);

        // account for receiving fees
        BigDecimal effectiveReceivedValue = oneThousand.add(receivingFees).divide(exchangeRate, 4, RoundingMode.HALF_UP)
                                                       .add(sendingFees);

        BigDecimal effectiveRate = BigDecimal.ONE.divide(effectiveReceivedValue, 7, RoundingMode.HALF_UP)
                                                 .multiply(oneThousand);
        return effectiveRate.toString();
    }

    private BigDecimal calculatePaySendFees(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(1000)) <= 0) {
            return BigDecimal.valueOf(4);
        }
        if (amount.compareTo(BigDecimal.valueOf(1500)) <= 0) {
            return BigDecimal.valueOf(5);
        }
        return BigDecimal.valueOf(6);
    }

    private BigDecimal calculateReceivingFees(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(0.005));
    }
}
