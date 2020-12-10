package com.bpwizard.gateway.plugin.base.utils;

import com.bpwizard.gateway.plugin.api.RemoteAddressResolver;
import org.springframework.web.server.ServerWebExchange;

/**
 * The type Host address utils.
 */
public class HostAddressUtils {

    /**
     * Acquire host string.
     *
     * @param exchange the exchange
     * @return the string
     */
    public static String acquireHost(final ServerWebExchange exchange) {
        return SpringBeanUtils.getInstance().getBean(RemoteAddressResolver.class).resolve(exchange).getHostString();
    }

    /**
     * Acquire ip string.
     *
     * @param exchange the exchange
     * @return the string
     */
    public static String acquireIp(final ServerWebExchange exchange) {
        return SpringBeanUtils.getInstance().getBean(RemoteAddressResolver.class).resolve(exchange).getAddress().getHostAddress();
    }
}
