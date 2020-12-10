package com.bpwizard.gateway.metrics.prometheus.impl.histogram;

import io.prometheus.client.Histogram;
import lombok.RequiredArgsConstructor;
import com.bpwizard.gateway.metrics.api.HistogramMetricsTrackerDelegate;


/**
 * Prometheus histogram metrics tracker delegate.
 */
@RequiredArgsConstructor
public final class PrometheusHistogramMetricsTrackerDelegate implements HistogramMetricsTrackerDelegate {
    
    private final Histogram.Timer timer;
    
    @Override
    public void observeDuration() {
        timer.observeDuration();
    }
}

