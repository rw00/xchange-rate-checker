package com.rw.apps.xchange.ratechecker.provider.whish.remitly.util;

import java.math.BigDecimal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EffectiveRateCalculatorTest {

    @ParameterizedTest
    @CsvSource({
            "1.2, 1.1862",
            "1.0, 0.9885"
    })
    void testCalculateEffectiveRate(BigDecimal exchangeRate, String expectedEffectiveRate) {
        String effectiveRate = EffectiveRateCalculator.calculateEffectiveRate(exchangeRate);
        assertEquals(expectedEffectiveRate, effectiveRate);
    }
}
