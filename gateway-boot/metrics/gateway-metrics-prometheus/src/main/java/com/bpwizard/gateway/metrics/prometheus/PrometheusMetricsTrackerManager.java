package com.bpwizard.gateway.metrics.prometheus;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.management.MalformedObjectNameException;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.bpwizard.gateway.metrics.api.MetricsTrackerFactory;
import com.bpwizard.gateway.metrics.config.MetricsConfig;
import com.bpwizard.gateway.metrics.spi.MetricsTrackerManager;
import com.bpwizard.gateway.spi.Join;

import com.bpwizard.gateway.metrics.prometheus.impl.collector.BuildInfoCollector;
import com.bpwizard.gateway.metrics.prometheus.impl.collector.JmxCollector;

/**
 * Prometheus metrics tracker manager.
 */
@Getter
@Slf4j
@Join
public final class PrometheusMetricsTrackerManager implements MetricsTrackerManager {
    
    private final MetricsTrackerFactory metricsTrackerFactory = new PrometheusMetricsTrackerFactory();
    
    private HTTPServer server;
    
    private volatile AtomicBoolean registered = new AtomicBoolean(false);
    
    @SneakyThrows(IOException.class)
    @Override
    public void start(final MetricsConfig metricsConfig) {
        register(metricsConfig.getJmxConfig());
        InetSocketAddress inetSocketAddress;
        if ("".equals(metricsConfig.getHost()) || null == metricsConfig.getHost()) {
            inetSocketAddress = new InetSocketAddress(metricsConfig.getPort());
        } else {
            inetSocketAddress = new InetSocketAddress(metricsConfig.getHost(), metricsConfig.getPort());
        }
        server = new HTTPServer(inetSocketAddress, CollectorRegistry.defaultRegistry, true);
        log.info("you start prometheus metrics http server  host is :{}, port is :{} ", inetSocketAddress.getHostString(), inetSocketAddress.getPort());
    }
    
    @Override
    public void stop() {
        server.stop();
    }
    
    private void register(final String jmxConfig) {
        if (!registered.compareAndSet(false, true)) {
            return;
        }
        new BuildInfoCollector().register();
        try {
            new JmxCollector(jmxConfig).register();
            DefaultExports.initialize();
        } catch (MalformedObjectNameException e) {
            log.error("init jxm collector error", e);
        }
    }
}

