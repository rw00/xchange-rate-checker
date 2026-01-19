package com.rw.apps.xchange.ratechecker.db;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * Manages the in-memory state of exchange rate providers.
 */
@Component
public class StatesDb {
    private final Map<String, ProviderState> providerStates = new ConcurrentHashMap<>();

    public ProviderState getOrCreateState(String providerName, double initialSpread) {
        return providerStates.computeIfAbsent(providerName, k -> new ProviderState(initialSpread));
    }

    public void updateState(String providerName, double spread) {
        ProviderState state = getOrCreateState(providerName, spread);
        state.setReferenceSpread(spread);
        state.setLastObservedSpread(spread);
    }
}
