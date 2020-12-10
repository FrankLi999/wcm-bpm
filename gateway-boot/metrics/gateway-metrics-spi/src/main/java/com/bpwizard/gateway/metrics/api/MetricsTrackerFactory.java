package com.bpwizard.gateway.metrics.api;

import java.util.Optional;

/**
 * Metrics tracker factory.
 */
public interface MetricsTrackerFactory {
    
    /**
     * Create of metrics tracker.
     *
     * @param metricsType metrics type
     * @param metricsLabel metrics label
     * @return metrics tracker
     */
    Optional<MetricsTracker> create(String metricsType, String metricsLabel);
}

