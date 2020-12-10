package com.bpwizard.gateway.metrics.prometheus.impl.counter;

import io.prometheus.client.Counter;
import com.bpwizard.gateway.metrics.api.CounterMetricsTracker;
import com.bpwizard.gateway.metrics.enums.MetricsLabelEnum;

/**
 * Request total counter metrics tracker.
 */
public final class RequestTotalCounterMetricsTracker implements CounterMetricsTracker {
    
    private static final Counter REQUEST_TOTAL = Counter.build()
            .name("request_total")
            .help("gateway request total count")
            .register();
    
    @Override
    public void inc(final double amount, final String... labelValues) {
        REQUEST_TOTAL.inc(amount);
    }
    
    @Override
    public String metricsLabel() {
        return MetricsLabelEnum.REQUEST_TOTAL.getName();
    }
}

