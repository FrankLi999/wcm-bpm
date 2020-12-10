package com.bpwizard.gateway.metrics.api;

/**
 * Summary metrics tracker delegate.
 */
public interface SummaryMetricsTrackerDelegate {
    
    /**
     * Observe amount of time in seconds since start time.
     */
    default void observeDuration() {
    }
}

