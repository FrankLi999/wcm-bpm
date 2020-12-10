package com.bpwizard.gateway.web.filter;

import com.bpwizard.gateway.plugin.api.result.GatewayResultEnum;
import com.bpwizard.gateway.plugin.base.utils.GatewayResultWrap;
import com.bpwizard.gateway.plugin.base.utils.WebFluxResultUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import com.bpwizard.gateway.web.config.GatewayConfig;

import reactor.core.publisher.Mono;

/**
 * this is visit time verify filter.
 * @deprecated do not use
 */
@Deprecated
public class TimeWebFilter extends AbstractWebFilter {

    private GatewayConfig gatewayConfig;

    public TimeWebFilter(final GatewayConfig gatewayConfig) {
        this.gatewayConfig = gatewayConfig;
    }

    @Override
    protected Mono<Boolean> doFilter(final ServerWebExchange exchange, final WebFilterChain chain) {
       /* final LocalDateTime start = requestDTO.getStartDateTime();
        final LocalDateTime now = LocalDateTime.now();
        final long between = DateUtils.acquireMinutesBetween(start, now);
        if (between < gatewayConfig.getFilterTime()) {
            return Mono.just(true);
        }*/
        return Mono.just(true);
    }

    @Override
    protected Mono<Void> doDenyResponse(final ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.REQUEST_TIMEOUT);
        Object error = GatewayResultWrap.error(GatewayResultEnum.TIME_ERROR.getCode(), GatewayResultEnum.TIME_ERROR.getMsg(), null);
        return WebFluxResultUtils.result(exchange, error);
    }
}
