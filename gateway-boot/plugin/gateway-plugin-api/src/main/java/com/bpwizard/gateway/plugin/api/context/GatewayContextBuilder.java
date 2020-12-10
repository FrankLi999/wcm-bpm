package com.bpwizard.gateway.plugin.api.context;

import org.springframework.web.server.ServerWebExchange;

/**
 * The interface Gateway context builder.
 */
public interface GatewayContextBuilder {

    /**
     * Build gateway context.
     *
     * @param exchange the exchange
     * @return the gateway context
     */
    GatewayContext build(ServerWebExchange exchange);
}
