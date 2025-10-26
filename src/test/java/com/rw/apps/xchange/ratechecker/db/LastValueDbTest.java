package com.rw.apps.xchange.ratechecker.db;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LastValueDbTest {
    private static final Instant NOW = Instant.now();
    private static final String OPEN_RATE = "1.11";
    private static final ExchangeRateComparison BASE_RECORD = new ExchangeRateComparison(OPEN_RATE,
                                                                                         "1.12",
                                                                                         "1.1567",
                                                                                         "1.13",
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
                                                                                 "1.22",
                                                                                 "1.1567",
                                                                                 "1.13",
                                                                                 NOW));

        assertTrue(updated);
    }

    @Test
    void shouldNotUpdateIfNewRateIsLessOrEqual() {
        lastValueDb.updateIfGreater(BASE_RECORD);

        boolean updated = lastValueDb.updateIfGreater(new ExchangeRateComparison(OPEN_RATE,
                                                                                 "1.09",
                                                                                 "1.1567",
                                                                                 "1.13",
                                                                                 NOW));

        assertFalse(updated);
    }

    @Test
    void shouldUpdateIfOpenRateDecreasedButNewRateIncreased() {
        boolean updated = lastValueDb.updateIfGreater(BASE_RECORD);
        assertTrue(updated);

        updated = lastValueDb.updateIfGreater(new ExchangeRateComparison("1.10", "1.06", "1.1567", "1.13", NOW));
        assertFalse(updated);

        updated = lastValueDb.updateIfGreater(new ExchangeRateComparison("1.08", "1.065", "1.1567", "1.13", NOW));
        assertTrue(updated);
    }

    @Test
    void shouldUpdateIfGapIsTooSmall() {
        boolean updated = lastValueDb.updateIfGreater(BASE_RECORD);
        assertTrue(updated);

        updated = lastValueDb.updateIfGreater(new ExchangeRateComparison("1.089", "1.08", "1.1567", "1.13", NOW));
        assertTrue(updated);
    }
}
