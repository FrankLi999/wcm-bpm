package com.bpwizard.gateway.metrics.prometheus;

import javax.management.MalformedObjectNameException;

import org.junit.jupiter.api.Test;

import com.bpwizard.gateway.metrics.prometheus.impl.collector.JmxCollector;

import io.prometheus.client.Collector;

/**
 * The type Jmx collector test.
 */
public final class JmxCollectorTest {
    
    /**
     * Test jmx collector.
     *
     * @throws MalformedObjectNameException the malformed object name exception
     */
    @Test
    public void testJmxCollector() throws MalformedObjectNameException {
        JmxCollector jc = new JmxCollector("{}".replace('`', '"'));
        for (Collector.MetricFamilySamples mfs : jc.collect()) {
            System.out.println(mfs);
        }
    }
}
