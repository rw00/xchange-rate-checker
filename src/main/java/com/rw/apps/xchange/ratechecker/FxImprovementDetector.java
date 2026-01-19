package com.rw.apps.xchange.ratechecker;

import com.rw.apps.xchange.ratechecker.db.StatesDb;
import com.rw.apps.xchange.ratechecker.db.ProviderRateAndSpread;
import com.rw.apps.xchange.ratechecker.db.ProviderState;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FxImprovementDetector {
    private final double baseImprovementThreshold;
    private final int decayDays;
    private final double decayFactor;
    private final StatesDb statesDb;

    public FxImprovementDetector(
            @Value("${threshold.base-improvement:0.005}") double baseImprovementThreshold,
            @Value("${threshold.decay-days:2}") int decayDays,
            @Value("${threshold.decay-factor:0.5}") double decayFactor, StatesDb statesDb) {
        this.baseImprovementThreshold = baseImprovementThreshold;
        this.decayDays = decayDays;
        this.decayFactor = decayFactor;
        this.statesDb = statesDb;
    }

    /**
     * Determines if any provider rate in the current snapshot warrants a notification.
     *
     * @param openRate The current open (mid-market) rate
     * @param providerData Map of provider names to their current rate and calculated spread
     * @param lastNotificationTime The timestamp of the last successful notification
     * @return true if at least one provider shows a superior rate or significant improvement
     */
    public boolean shouldNotify(double openRate, Map<String, ProviderRateAndSpread> providerData,
            java.time.Instant lastNotificationTime) {
        double currentThreshold = calculateCurrentThreshold(lastNotificationTime);

        return providerData.entrySet().stream().map(entry -> processProvider(entry.getKey(),
                entry.getValue(), openRate, currentThreshold)).reduce(false, (a, b) -> a || b);
    }

    private double calculateCurrentThreshold(java.time.Instant lastNotificationTime) {
        if (lastNotificationTime == null) {
            return baseImprovementThreshold;
        }

        long millisPassed = java.time.Duration
                .between(lastNotificationTime, java.time.Instant.now()).toMillis();
        double daysPassed = millisPassed / (1000.0 * 60 * 60 * 24);

        if (daysPassed <= 0) {
            return baseImprovementThreshold;
        }

        // exponential decay: threshold = base * (factor ^ (days / decayDays))
        return baseImprovementThreshold * Math.pow(decayFactor, daysPassed / decayDays);
    }

    private boolean processProvider(String name, ProviderRateAndSpread data, double openRate,
            double threshold) {
        double providerRateVal = Double.parseDouble(data.rate());
        double currentSpread = data.spread();

        // 1. Superior rate detection
        if (providerRateVal >= openRate) {
            statesDb.updateState(name, currentSpread);
            return true;
        }

        // 2. Significant improvement detection
        ProviderState state = statesDb.getOrCreateState(name, currentSpread);
        double improvement = state.getReferenceSpread() - currentSpread;

        boolean isSignificant = improvement >= threshold;
        if (isSignificant) {
            state.setReferenceSpread(currentSpread);
        }

        // Always update last observed for monitoring
        state.setLastObservedSpread(currentSpread);

        return isSignificant;
    }
}
