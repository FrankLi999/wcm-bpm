package com.bpwizard.gateway.metrics.api;

import com.bpwizard.gateway.metrics.enums.MetricsTypeEnum;

/**
 * Gauge metrics tracker interface.
 */
public interface GaugeMetricsTracker extends MetricsTracker {
    
    /**
     *  Increment the Gauge with label values by the given amount.
     *
     * @param amount amount
     * @param labelValues label values
     */
    void inc(double amount, String... labelValues);
    
    /**
     *  Decrement the Gauge with label values by the given amount.
     *
     * @param amount amount
     * @param labelValues label values
     */
    void dec(double amount, String... labelValues);
    
    /**
     * Metrics type.
     *
     * @return metrics type
     */
    default String metricsType() {
        return MetricsTypeEnum.GAUGE.name();
    }
}

