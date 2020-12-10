package com.bpwizard.gateway.metrics.prometheus.impl.summary;

import io.prometheus.client.Summary;
import lombok.RequiredArgsConstructor;
import com.bpwizard.gateway.metrics.api.SummaryMetricsTrackerDelegate;

/**
 * Prometheus summary metrics tracker delegate.
 */
@RequiredArgsConstructor
public final class PrometheusSummaryMetricsTrackerDelegate implements SummaryMetricsTrackerDelegate {
    
    private final Summary.Timer timer;
    
    @Override
    public void observeDuration() {
        timer.observeDuration();
    }
}

