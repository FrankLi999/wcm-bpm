package com.bpwizard.gateway.common.config;

import java.util.Properties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * The monitor configuration for influxdb.
 */
@Data
@EqualsAndHashCode
public class MonitorConfig implements Serializable {
    
	private static final long serialVersionUID = 4574901502704515127L;

	private String metricsName;
    
    private String host;
    
    private Integer port;
    
    private Boolean async;
    
    private Integer threadCount;
    
    private Properties props;

}
