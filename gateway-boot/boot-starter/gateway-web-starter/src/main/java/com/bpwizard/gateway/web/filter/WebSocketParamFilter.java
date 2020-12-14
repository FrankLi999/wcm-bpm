package com.bpwizard.gateway.web.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import com.bpwizard.gateway.common.constant.Constants;
import com.bpwizard.gateway.common.enums.RpcTypeEnum;
import com.bpwizard.gateway.plugin.api.result.GatewayResultEnum;
import com.bpwizard.gateway.plugin.base.utils.GatewayResultWrap;
import com.bpwizard.gateway.plugin.base.utils.WebFluxResultUtils;

import reactor.core.publisher.Mono;

/**
 * this is http post param verify filter.
 */
public class WebSocketParamFilter extends AbstractWebFilter {

    @Override
    protected Mono<Boolean> doFilter(final ServerWebExchange exchange, final WebFilterChain chain) {
        final ServerHttpRequest request = exchange.getRequest();
        final HttpHeaders headers = request.getHeaders();
        final String upgrade = headers.getFirst("Upgrade");
        if (StringUtils.isNoneBlank(upgrade) && RpcTypeEnum.WEB_SOCKET.getName().equals(upgrade)) {
            return Mono.just(verify(request.getQueryParams()));
        }
        return Mono.just(true);
    }

    @Override
    protected Mono<Void> doDenyResponse(final ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        Object error = GatewayResultWrap.error(GatewayResultEnum.PARAM_ERROR.getCode(), GatewayResultEnum.PARAM_ERROR.getMsg(), null);
        return WebFluxResultUtils.result(exchange, error);
    }

    private Boolean verify(final MultiValueMap<String, String> queryParams) {
        return !StringUtils.isBlank(queryParams.getFirst(Constants.MODULE))
                && !StringUtils.isBlank(queryParams.getFirst(Constants.METHOD))
                && !StringUtils.isBlank(queryParams.getFirst(Constants.RPC_TYPE));
    }

}
