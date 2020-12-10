package com.bpwizard.gateway.spring.boot.starter.sync.data.websocket;

import lombok.extern.slf4j.Slf4j;
import com.bpwizard.gateway.plugin.sync.data.weboscket.WebsocketSyncDataService;
import com.bpwizard.gateway.plugin.sync.data.weboscket.config.WebsocketConfig;
import com.bpwizard.gateway.sync.data.api.AuthDataSubscriber;
import com.bpwizard.gateway.sync.data.api.MetaDataSubscriber;
import com.bpwizard.gateway.sync.data.api.PluginDataSubscriber;
import com.bpwizard.gateway.sync.data.api.SyncDataService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

/**
 * Websocket sync data configuration for spring boot.
 */
@Configuration
@ConditionalOnClass(WebsocketSyncDataService.class)
@ConditionalOnProperty(prefix = "gateway.sync.websocket", name = "urls")
@Slf4j
public class WebsocketSyncDataConfiguration {

    /**
     * Websocket sync data service.
     *
     * @param websocketConfig   the websocket config
     * @param pluginSubscriber the plugin subscriber
     * @param metaSubscribers   the meta subscribers
     * @param authSubscribers   the auth subscribers
     * @return the sync data service
     */
    @Bean
    public SyncDataService websocketSyncDataService(final ObjectProvider<WebsocketConfig> websocketConfig, final ObjectProvider<PluginDataSubscriber> pluginSubscriber,
                                           final ObjectProvider<List<MetaDataSubscriber>> metaSubscribers, final ObjectProvider<List<AuthDataSubscriber>> authSubscribers) {
        log.info("you use websocket sync gateway data.......");
        return new WebsocketSyncDataService(websocketConfig.getIfAvailable(WebsocketConfig::new), pluginSubscriber.getIfAvailable(),
                metaSubscribers.getIfAvailable(Collections::emptyList), authSubscribers.getIfAvailable(Collections::emptyList));
    }

    /**
     * Config websocket config.
     *
     * @return the websocket config
     */
    @Bean
    @ConfigurationProperties(prefix = "gateway.sync.websocket")
    public WebsocketConfig websocketConfig() {
        return new WebsocketConfig();
    }
}
