package com.bpwizard.gateway.metrics.spi;

import com.bpwizard.gateway.spi.SPI;
import com.bpwizard.gateway.metrics.api.MetricsTrackerFactory;
import com.bpwizard.gateway.metrics.config.MetricsConfig;

/**
 * Metrics tracker manager.
 */
@SPI
public interface MetricsTrackerManager {
    
    /**
     * Start metrics tracker.
     *
     * @param metricsConfig metrics config
     */
    void start(MetricsConfig metricsConfig);
    
    /**
     * Gets metrics tracker factory.
     *
     * @return metrics tracker factory
     */
    MetricsTrackerFactory getMetricsTrackerFactory();
    
    /**
     * Stop metrics tracker.
     */
    void stop();
}

