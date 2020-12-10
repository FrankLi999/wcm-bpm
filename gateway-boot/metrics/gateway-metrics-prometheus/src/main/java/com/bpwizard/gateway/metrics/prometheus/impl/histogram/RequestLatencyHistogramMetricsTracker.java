package com.bpwizard.gateway.metrics.prometheus.impl.histogram;

import io.prometheus.client.Histogram;
import com.bpwizard.gateway.metrics.api.HistogramMetricsTracker;
import com.bpwizard.gateway.metrics.api.HistogramMetricsTrackerDelegate;
import com.bpwizard.gateway.metrics.enums.MetricsLabelEnum;

/**
 * Request latency histogram metrics tracker.
 */
public final class RequestLatencyHistogramMetricsTracker implements HistogramMetricsTracker {
    
    private static final Histogram REQUEST_LATENCY = Histogram.build()
            .name("requests_latency_histogram_millis").help("Requests Latency Histogram Millis (ms)")
            .register();
    
    @Override
    public HistogramMetricsTrackerDelegate startTimer(final String... labelValues) {
        Histogram.Timer timer = REQUEST_LATENCY.startTimer();
        return new PrometheusHistogramMetricsTrackerDelegate(timer);
    }
    
    @Override
    public String metricsLabel() {
        return MetricsLabelEnum.REQUEST_LATENCY.getName();
    }
}

