package com.bpwizard.gateway.metrics.prometheus;

import com.bpwizard.gateway.metrics.api.MetricsTracker;
import com.bpwizard.gateway.metrics.api.MetricsTrackerFactory;

import com.bpwizard.gateway.metrics.prometheus.impl.counter.HttpRequestCounterMetricsTracker;
import com.bpwizard.gateway.metrics.prometheus.impl.counter.RequestTotalCounterMetricsTracker;
import com.bpwizard.gateway.metrics.prometheus.impl.histogram.RequestLatencyHistogramMetricsTracker;
import com.bpwizard.gateway.metrics.prometheus.impl.summary.RequestLatencySummaryMetricsTracker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Prometheus metrics tracker factory.
 */
public final class PrometheusMetricsTrackerFactory implements MetricsTrackerFactory {
    
    private static final Collection<MetricsTracker> REGISTER = new ArrayList<>();
    
    static {
        REGISTER.add(new RequestTotalCounterMetricsTracker());
        REGISTER.add(new HttpRequestCounterMetricsTracker());
        REGISTER.add(new RequestLatencyHistogramMetricsTracker());
        REGISTER.add(new RequestLatencySummaryMetricsTracker());
    }
    
    @Override
    public Optional<MetricsTracker> create(final String metricsType, final String metricsLabel) {
        return REGISTER.stream().filter(each -> each.metricsLabel().equals(metricsLabel) && each.metricsType().equals(metricsType)).findFirst();
    }
}

