package com.bpwizard.gateway.plugin.api;

import org.springframework.web.server.ServerWebExchange;

import java.net.InetSocketAddress;

/**
 * The interface Remote address resolver.
 */
public interface RemoteAddressResolver {

    /**
     * Resolve inet socket address.
     *
     * @param exchange the exchange
     * @return the inet socket address
     */
    default InetSocketAddress resolve(ServerWebExchange exchange) {
        return exchange.getRequest().getRemoteAddress();
    }

}
