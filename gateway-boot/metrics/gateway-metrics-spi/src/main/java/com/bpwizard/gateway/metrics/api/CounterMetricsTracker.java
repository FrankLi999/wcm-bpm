package com.bpwizard.gateway.metrics.api;

import com.bpwizard.gateway.metrics.enums.MetricsTypeEnum;

/**
 * Counter metrics tracker interface.
 */
public interface CounterMetricsTracker extends MetricsTracker {
    
    /**
     * Increment the counter with label values by the given amount.
     *
     * @param amount      amount
     * @param labelValues label values
     */
    void inc(double amount, String... labelValues);
    
    /**
     * Metrics type.
     *
     * @return metrics type
     */
    default String metricsType() {
        return MetricsTypeEnum.COUNTER.name();
    }
}

