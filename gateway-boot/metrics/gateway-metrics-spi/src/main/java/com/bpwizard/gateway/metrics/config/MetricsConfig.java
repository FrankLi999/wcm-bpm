package com.bpwizard.gateway.metrics.config;

import java.io.Serializable;
import java.util.Properties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Metrics config.
 */
@AllArgsConstructor
@Data
@EqualsAndHashCode
public final class MetricsConfig implements Serializable {

	private static final long serialVersionUID = -4779964738782499672L;

	private String metricsName;
    
    private String host;
    
    private Integer port;
    
    private Boolean async;
    
    private Integer threadCount;
    
    private String jmxConfig;
    
    private Properties props;
    
}

