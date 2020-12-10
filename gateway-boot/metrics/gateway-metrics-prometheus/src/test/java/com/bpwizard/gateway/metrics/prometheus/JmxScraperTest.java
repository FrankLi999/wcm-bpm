package com.bpwizard.gateway.metrics.prometheus;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.management.ObjectName;

import org.junit.jupiter.api.Test;

import com.bpwizard.gateway.metrics.prometheus.impl.collector.JmxMBeanPropertyCache;
import com.bpwizard.gateway.metrics.prometheus.impl.collector.JmxScraper;
import com.bpwizard.gateway.metrics.prometheus.impl.collector.MBeanReceiver;

/**
 * The type Jmx scraper test.
 */
public final class JmxScraperTest {
    
    /**
     * Test jmx scraper test.
     *
     * @throws Exception the exception
     */
    @Test
    public void testJmxScraperTest() throws Exception {
        List<ObjectName> objectNames = new LinkedList<>();
        new JmxScraper("", "", "", false, objectNames, new LinkedList<>(),
                new StdoutWriter(), new JmxMBeanPropertyCache()).doScrape();
    }
    
    private static class StdoutWriter implements MBeanReceiver {
        
        public void recordBean(final String domain, final Map<String, String> beanProperties,
                               final LinkedList<String> attrKeys, final String attrName,
                               final String attrType, final String attrDescription, final Object value) {
            System.out.println(domain + attrKeys + attrName + ": " + value);
        }
    }
}
