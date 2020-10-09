package com.bpwizard.gateway.admin.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * the websocket sync strategy properties.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "gateway.sync.websocket")
public class WebsocketSyncProperties {
    
    /**
     * default: true.
     */
    private boolean enabled = true;
    
}
