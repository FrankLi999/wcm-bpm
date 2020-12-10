package com.bpwizard.gateway.metrics.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Metrics label enum.
 */
@RequiredArgsConstructor
@Getter
public enum MetricsLabelEnum {
    
    /**
     * Request total metrics label.
     */
    REQUEST_TOTAL("request_total"),
    
    /**
     * http request total metrics label.
     */
    HTTP_REQUEST_TOTAL("http_request_total"),
    
    /**
     * Request latency metrics label.
     */
    REQUEST_LATENCY("request_latency");
    
    private final String name;
}
