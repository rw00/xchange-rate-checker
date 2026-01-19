package com.rw.apps.xchange.ratechecker.provider.whish;

import java.math.BigDecimal;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FeeCalculator {
    // 1% fee on received amount (cash-out)
    private final BigDecimal WHISH_FEE_MULTIPLIER = BigDecimal.valueOf(0.01);

    public BigDecimal applyWhishFee(BigDecimal receivedAmount) {
        return receivedAmount.multiply(BigDecimal.ONE.subtract(WHISH_FEE_MULTIPLIER));
    }
}
