package com.bpwizard.gateway.metrics.prometheus.impl.counter;

import io.prometheus.client.Counter;
import com.bpwizard.gateway.metrics.api.CounterMetricsTracker;
import com.bpwizard.gateway.metrics.enums.MetricsLabelEnum;

/**
 * Request total counter metrics tracker.
 */
public final class HttpRequestCounterMetricsTracker implements CounterMetricsTracker {
    
    private static final Counter HTTP_REQUEST_TOTAL = Counter.build()
            .name("http_request_total")
            .labelNames("path", "type")
            .help("gateway http request type total count")
            .register();
    
    @Override
    public void inc(final double amount, final String... labelValues) {
        HTTP_REQUEST_TOTAL.labels(labelValues).inc(amount);
    }
    
    @Override
    public String metricsLabel() {
        return MetricsLabelEnum.HTTP_REQUEST_TOTAL.getName();
    }
}

