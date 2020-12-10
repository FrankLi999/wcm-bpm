package com.bpwizard.gateway.metrics.prometheus.impl.collector;

import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The type Build info collector.
 */
public class BuildInfoCollector extends Collector {
    
    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfs = new ArrayList<>();
        GaugeMetricFamily artifactInfo = new GaugeMetricFamily(
                "jmx_exporter_build_info",
                "A metric with a constant '1' value labeled with the version of the JMX exporter.",
                Arrays.asList("version", "name"));
        Package pkg = this.getClass().getPackage();
        String version = pkg.getImplementationVersion();
        String name = pkg.getImplementationTitle();
        artifactInfo.addMetric(Arrays.asList(version != null ? version : "unknown", name != null ? name : "unknown"), 1L);
        mfs.add(artifactInfo);
        return mfs;
    }
}
