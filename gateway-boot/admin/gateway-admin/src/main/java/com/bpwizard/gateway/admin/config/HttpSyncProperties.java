package com.bpwizard.gateway.admin.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * the http sync strategy properties.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "gateway.sync.http")
public class HttpSyncProperties {

    /**
     * if enable http sync, default: true.
     */
    private boolean enabled = true;

    /**
     * Periodically refresh the config data interval from the database, default: 5 minutes.
     */
    private Duration refreshInterval = Duration.ofMinutes(5);

}
