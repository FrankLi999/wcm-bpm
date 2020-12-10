package com.bpwizard.gateway.metrics.api;

import com.bpwizard.gateway.metrics.enums.MetricsTypeEnum;

/**
 * Summary metrics tracker.
 */
public interface SummaryMetricsTracker extends MetricsTracker {
    
    /**
     * Start timer with summary.
     *
     * @param labelValues label values
     * @return Summary metrics tracker delegate
     */
    default SummaryMetricsTrackerDelegate startTimer(String... labelValues) {
        return new NoneSummaryMetricsTrackerDelegate();
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
        return MetricsTypeEnum.SUMMARY.name();
    }
}
