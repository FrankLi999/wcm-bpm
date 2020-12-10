package com.bpwizard.gateway.metrics.api;

import com.bpwizard.gateway.metrics.enums.MetricsTypeEnum;

/**
 * Histogram metrics tracker.
 */
public interface HistogramMetricsTracker extends MetricsTracker {
    
    /**
     * Start timer with histogram.
     *
     * @param labelValues label values
     * @return histogram metrics tracker delegate
     */
    default HistogramMetricsTrackerDelegate startTimer(String... labelValues) {
        return new NoneHistogramMetricsTrackerDelegate();
    }
    
    /**
     * Observe the given amount.
     *
     * @param amount amount
     */
    default void observer(long amount) {
    }
    
    /**
     * Metrics type.
     *
     * @return metrics type
     */
    default String metricsType() {
        return MetricsTypeEnum.HISTOGRAM.name();
    }
}

