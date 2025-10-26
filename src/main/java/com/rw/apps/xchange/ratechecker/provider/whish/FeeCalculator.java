package com.rw.apps.xchange.ratechecker.provider.whish;

import java.math.BigDecimal;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FeeCalculator {
    private final BigDecimal WHISH_FEE_MULTIPLIER = BigDecimal.valueOf(0.01); // 1% fee on received amount

    public BigDecimal applyWhishFee(BigDecimal receivedAmount) {
        return receivedAmount.multiply(BigDecimal.ONE.subtract(WHISH_FEE_MULTIPLIER));
    }
}
