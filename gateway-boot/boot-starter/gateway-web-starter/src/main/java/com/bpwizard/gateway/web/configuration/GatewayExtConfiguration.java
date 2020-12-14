package com.bpwizard.gateway.web.configuration;

import com.bpwizard.gateway.plugin.api.RemoteAddressResolver;
import com.bpwizard.gateway.plugin.api.result.DefaultGatewayResult;
import com.bpwizard.gateway.plugin.api.result.GatewayResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.annotation.Bean;

/**
 * The type Gateway result configuration.
 */
public class GatewayExtConfiguration {

    /**
     * Gateway result gateway result.
     *
     * @return the gateway result
     */
    @SuppressWarnings("rawtypes")
	@Bean
    @ConditionalOnMissingBean(value = GatewayResult.class, search = SearchStrategy.ALL)
    public GatewayResult gatewayResult() {
        return new DefaultGatewayResult();
    }

    /**
     * Remote address resolver remote address resolver.
     *
     * @return the remote address resolver
     */
    @Bean
    @ConditionalOnMissingBean(value = RemoteAddressResolver.class, search = SearchStrategy.ALL)
    public RemoteAddressResolver remoteAddressResolver() {
        return new RemoteAddressResolver() {
        };
    }

}
