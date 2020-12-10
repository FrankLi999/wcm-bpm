package com.bpwizard.gateway.metrics.api;

/**
 * Histogram metrics tracker delegate.
 */
public interface HistogramMetricsTrackerDelegate {
    
    /**
     * Observe amount of time since start time.
     */
    default void observeDuration() {
    }
}

