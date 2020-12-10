package com.bpwizard.gateway.plugin.base.fallback;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.ServerWebExchange;

import com.bpwizard.gateway.plugin.base.utils.SpringBeanUtils;

import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Objects;

/**
 * Fallback handler.
 */
public interface FallbackHandler {

    /**
     * do without fallback uri.
     *
     * @param exchange  the web exchange
     * @param throwable the throwable
     * @return mono
     */
    Mono<Void> generateError(ServerWebExchange exchange, Throwable throwable);

    /**
     * do fallback.
     *
     * @param exchange the web exchange
     * @param uri      the uri
     * @param t        the throwable
     * @return Mono
     */
    default Mono<Void> fallback(ServerWebExchange exchange, URI uri, Throwable t) {
        if (Objects.isNull(uri)) {
            return generateError(exchange, t);
        }
        DispatcherHandler dispatcherHandler = SpringBeanUtils.getInstance().getBean(DispatcherHandler.class);
        ServerHttpRequest request = exchange.getRequest().mutate().uri(Objects.requireNonNull(uri)).build();
        ServerWebExchange mutated = exchange.mutate().request(request).build();
        return dispatcherHandler.handle(mutated);
    }
}
