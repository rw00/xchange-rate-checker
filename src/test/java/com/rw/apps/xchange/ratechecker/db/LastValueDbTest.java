package com.rw.apps.xchange.ratechecker.db;

import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LastValueDbTest {
    private static final Instant NOW = Instant.now();
    private static final String OPEN_RATE = "1.11";
    private static final ExchangeRateComparison BASE_RECORD = new ExchangeRateComparison(OPEN_RATE,
            Map.of("TapTapSend", "1.12",
                    "Remitly Whish", "1.1567",
                    "Wise Whish", "1.13"),
            NOW);
    private final LastValueDb lastValueDb = new LastValueDb();

    @Test
    void shouldUpdateWhenLastValueIsNull() {
        boolean updated = lastValueDb.updateIfGreater(BASE_RECORD);

        assertTrue(updated);
    }

    @Test
    void shouldUpdateIfNewRateIsGreater() {
        lastValueDb.updateIfGreater(BASE_RECORD);

        boolean updated = lastValueDb.updateIfGreater(new ExchangeRateComparison(OPEN_RATE,
                Map.of("TapTapSend", "1.22",
                        "Remitly Whish", "1.1567",
                        "Wise Whish", "1.13"),
                NOW));

        assertTrue(updated);
    }

    @Test
    void shouldNotUpdateIfNewRateIsLessOrEqual() {
        lastValueDb.updateIfGreater(BASE_RECORD);

        boolean updated = lastValueDb.updateIfGreater(new ExchangeRateComparison(OPEN_RATE,
                Map.of("TapTapSend", "1.09",
                        "Remitly Whish", "1.1567",
                        "Wise Whish", "1.13"),
                NOW));

        assertFalse(updated);
    }

    @Test
    void shouldUpdateIfOpenRateDecreasedButNewRateIncreased() {
        boolean updated = lastValueDb.updateIfGreater(BASE_RECORD);
        assertTrue(updated);

        // All rates decreased compared to BASE_RECORD
        // but increase compared to last values check
        updated = lastValueDb.updateIfGreater(new ExchangeRateComparison("1.10", Map.of("TapTapSend", "1.06",
                "Remitly Whish", "1.1567",
                "Wise Whish", "1.13"), NOW));
        assertFalse(updated);

        // Open rate 1.08, TapTapSend 1.065.
        // compared to previous lastValue (1.06): increased.
        updated = lastValueDb.updateIfGreater(new ExchangeRateComparison("1.08", Map.of("TapTapSend", "1.065",
                "Remitly Whish", "1.1567",
                "Wise Whish", "1.13"), NOW));
        assertTrue(updated);
    }

    @Test
    void shouldUpdateIfGapIsTooSmall() {
        boolean updated = lastValueDb.updateIfGreater(BASE_RECORD);
        assertTrue(updated);

        // Open: 1.089, Provider: 1.08.
        // gap = 0.009 < 0.01 ; should update.
        updated = lastValueDb.updateIfGreater(new ExchangeRateComparison("1.089", Map.of("TapTapSend", "1.08",
                "Remitly Whish", "1.1500",
                "Wise Whish", "1.13"), NOW));
        assertTrue(updated);
    }
}
