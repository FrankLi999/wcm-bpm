package com.bpwizard.gateway.metrics.api;

/**
 * Metrics tracker.
 */
public interface MetricsTracker {
    
    /**
     * Metrics label.
     *
     * @return metrics label
     */
    String metricsLabel();
    
    /**
     * Metrics type.
     *
     * @return metrics type
     */
    String metricsType();
}

