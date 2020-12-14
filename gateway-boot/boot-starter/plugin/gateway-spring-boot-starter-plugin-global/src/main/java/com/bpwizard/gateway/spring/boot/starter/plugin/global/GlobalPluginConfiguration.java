package com.bpwizard.gateway.spring.boot.starter.plugin.global;

import com.bpwizard.gateway.plugin.api.GatewayPlugin;
import com.bpwizard.gateway.plugin.api.context.GatewayContextBuilder;
import com.bpwizard.gateway.plugin.global.DefaultGatewayContextBuilder;
import com.bpwizard.gateway.plugin.global.GlobalPlugin;
import com.bpwizard.gateway.plugin.global.subsciber.MetaDataAllSubscriber;
import com.bpwizard.gateway.sync.data.api.MetaDataSubscriber;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Global plugin configuration.
 */
@Configuration
@ConditionalOnClass(GlobalPlugin.class)
public class GlobalPluginConfiguration {

    /**
     * Global plugin gateway plugin.
     *
     * @param gatewayContextBuilder the gateway context builder
     * @return the gateway plugin
     */
    @Bean
    public GatewayPlugin globalPlugin(final GatewayContextBuilder gatewayContextBuilder) {
        return new GlobalPlugin(gatewayContextBuilder);
    }

    /**
     * Gateway context builder gateway context builder.
     *
     * @return the gateway context builder
     */
    @Bean
    @ConditionalOnMissingBean(value = GatewayContextBuilder.class, search = SearchStrategy.ALL)
    public GatewayContextBuilder gatewayContextBuilder() {
        return new DefaultGatewayContextBuilder();
    }

    /**
     * Data subscriber meta data subscriber.
     *
     * @return the meta data subscriber
     */
    @Bean
    public MetaDataSubscriber metaDataAllSubscriber() {
        return new MetaDataAllSubscriber();
    }
}
