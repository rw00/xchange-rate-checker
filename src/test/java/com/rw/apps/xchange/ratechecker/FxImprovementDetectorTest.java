package com.rw.apps.xchange.ratechecker;

import com.rw.apps.xchange.ratechecker.db.StatesDb;
import com.rw.apps.xchange.ratechecker.db.ProviderRateAndSpread;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FxImprovementDetectorTest {
        private static final String PROVIDER = "Provider";
        private static final double OPEN_RATE = 1.11;

        private final StatesDb statesDb = new StatesDb();
        private final FxImprovementDetector detector =
                        new FxImprovementDetector(0.005, 4, 0.5, statesDb);

        @Test
        void shouldNotifyWhenProviderRateMatchesOrExceedsOpenRate() {
                var data = Map.of(PROVIDER, new ProviderRateAndSpread("1.12", -0.01));

                assertTrue(detector.shouldNotify(OPEN_RATE, data, null));
        }

        @Test
        void shouldNotNotifyOnFirstObservationIfRateIsNormal() {
                var data = Map.of(PROVIDER, new ProviderRateAndSpread("1.10", 0.01));

                assertFalse(detector.shouldNotify(OPEN_RATE, data, null));
        }

        @Test
        void shouldNotifyOnSignificantImprovement() {
                // First observation: spread 0.01
                detector.shouldNotify(OPEN_RATE,
                                Map.of(PROVIDER, new ProviderRateAndSpread("1.10", 0.01)), null);

                // Spread narrowed from 0.01 to 0.004 (improvement of 0.006 > 0.005 threshold)
                var improvedData = Map.of(PROVIDER, new ProviderRateAndSpread("1.106", 0.004));

                assertTrue(detector.shouldNotify(OPEN_RATE, improvedData, null));
        }

        @Test
        void shouldNotNotifyOnInsignificantImprovement() {
                // First observation: spread 0.01
                detector.shouldNotify(OPEN_RATE,
                                Map.of(PROVIDER, new ProviderRateAndSpread("1.10", 0.01)), null);

                // Spread narrowed from 0.01 to 0.006 (improvement of 0.004 < 0.005 threshold)
                var improvedData = Map.of(PROVIDER, new ProviderRateAndSpread("1.104", 0.006));

                assertFalse(detector.shouldNotify(OPEN_RATE, improvedData, null));
        }

        @Test
        void shouldNotifyOnInsignificantImprovementIfThresholdDecayed() {
                java.time.Instant fourDaysAgo =
                                java.time.Instant.now().minus(java.time.Duration.ofDays(4));

                // First observation: spread 0.01
                detector.shouldNotify(OPEN_RATE,
                                Map.of(PROVIDER, new ProviderRateAndSpread("1.10", 0.01)), null);

                // Spread narrowed from 0.01 to 0.007 (improvement of 0.003)
                // Threshold was 0.005, but after 4 days it should be 0.005 * 0.5 = 0.0025
                var improvedData = Map.of(PROVIDER, new ProviderRateAndSpread("1.103", 0.007));

                assertTrue(detector.shouldNotify(OPEN_RATE, improvedData, fourDaysAgo));
        }
}
