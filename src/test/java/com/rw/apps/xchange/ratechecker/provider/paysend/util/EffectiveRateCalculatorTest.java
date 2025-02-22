package com.rw.apps.xchange.ratechecker.provider.paysend.util;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EffectiveRateCalculatorTest {

    @Test
    void test() {
        String effectiveRate = EffectiveRateCalculator.calculateEffectiveRate(BigDecimal.valueOf(1.0327));
        assertEquals("1.0234000", effectiveRate);
    }
}
