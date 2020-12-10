package com.bpwizard.gateway.metrics.prometheus.impl.summary;

import io.prometheus.client.Summary;
import com.bpwizard.gateway.metrics.api.SummaryMetricsTracker;
import com.bpwizard.gateway.metrics.api.SummaryMetricsTrackerDelegate;
import com.bpwizard.gateway.metrics.enums.MetricsLabelEnum;

import java.util.concurrent.TimeUnit;

/**
 * Request latency summary metrics tracker.
 */
public final class RequestLatencySummaryMetricsTracker implements SummaryMetricsTracker {
    
    private static final Summary REQUEST_LATENCY = Summary.build()
            .name("requests_latency_summary_millis").help("Requests Latency Summary Millis (ms)")
            .quantile(0.5, 0.05)
            .quantile(0.95, 0.01)
            .quantile(0.99, 0.001)
            .maxAgeSeconds(TimeUnit.MINUTES.toSeconds(5))
            .ageBuckets(5)
            .register();
    
    @Override
    public SummaryMetricsTrackerDelegate startTimer(final String... labelValues) {
        Summary.Timer timer = REQUEST_LATENCY.startTimer();
        return new PrometheusSummaryMetricsTrackerDelegate(timer);
    }
    
    @Override
    public String metricsLabel() {
        return MetricsLabelEnum.REQUEST_LATENCY.getName();
    }
}

