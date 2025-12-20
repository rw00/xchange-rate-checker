package com.rw.apps.xchange.ratechecker.provider.whish.wise.util;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EffectiveRateCalculatorTest {

    @Test
    void test() {
        String effectiveRate = EffectiveRateCalculator.calculateEffectiveRate(new BigDecimal("1.1587"));
        assertEquals("1.1432", effectiveRate);
    }
}
