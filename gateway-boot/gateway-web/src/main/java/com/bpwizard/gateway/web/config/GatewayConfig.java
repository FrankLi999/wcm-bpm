package com.bpwizard.gateway.web.config;

import lombok.Data;

/**
 * The type Gateway config.
 */
@Data
public class GatewayConfig {

    private Integer filterTime = 10;

    private Boolean filterTimeEnable = false;

    private Integer upstreamScheduledTime = 30;

}
