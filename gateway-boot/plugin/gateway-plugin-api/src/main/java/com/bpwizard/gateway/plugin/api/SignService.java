package com.bpwizard.gateway.plugin.api;

import org.springframework.web.server.ServerWebExchange;

import org.apache.commons.lang3.tuple.Pair;

/**
 * The interface Sign service.
 */
public interface SignService {

    /**
     * Sign verify pair.
     * @param exchange   the exchange
     * @return the pair
     */
	Pair<Boolean, String> signVerify(ServerWebExchange exchange);
}
