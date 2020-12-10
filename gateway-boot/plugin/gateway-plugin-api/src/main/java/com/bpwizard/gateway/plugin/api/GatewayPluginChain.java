package com.bpwizard.gateway.plugin.api;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * the gateway plugin chain.
 */
public interface GatewayPluginChain {

    /**
     * Delegate to the next {@code WebFilter} in the chain.
     *
     * @param exchange the current server exchange
     * @return {@code Mono<Void>} to indicate when request handling is complete
     */
    Mono<Void> execute(ServerWebExchange exchange);

}
