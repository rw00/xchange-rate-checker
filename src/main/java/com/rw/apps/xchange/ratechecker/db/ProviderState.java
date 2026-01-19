package com.rw.apps.xchange.ratechecker.db;

public class ProviderState {
    private double referenceSpread;
    private double lastObservedSpread;

    public ProviderState(double initialSpread) {
        this.referenceSpread = initialSpread;
        this.lastObservedSpread = initialSpread;
    }

    public double getReferenceSpread() {
        return referenceSpread;
    }

    public void setReferenceSpread(double referenceSpread) {
        this.referenceSpread = referenceSpread;
    }

    public double getLastObservedSpread() {
        return lastObservedSpread;
    }

    public void setLastObservedSpread(double lastObservedSpread) {
        this.lastObservedSpread = lastObservedSpread;
    }
}
