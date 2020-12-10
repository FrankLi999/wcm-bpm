package com.bpwizard.gateway.sync.data.http.config;

import lombok.Data;

/**
 * The type Http config.
 */
@Data
public class HttpConfig {
    
    private String url;
    
    private Integer delayTime;
    
    private Integer connectionTimeout;
}
