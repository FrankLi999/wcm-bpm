package com.bpwizard.gateway.plugin.global;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import com.bpwizard.gateway.common.constant.Constants;
import com.bpwizard.gateway.plugin.api.GatewayPlugin;
import com.bpwizard.gateway.plugin.api.GatewayPluginChain;
import com.bpwizard.gateway.plugin.api.context.GatewayContext;
import com.bpwizard.gateway.plugin.api.context.GatewayContextBuilder;

import reactor.core.publisher.Mono;

/**
 * The type Global plugin.
 */
public class GlobalPlugin implements GatewayPlugin {
    
    private GatewayContextBuilder builder;
    
    /**
     * Instantiates a new Global plugin.
     *
     * @param builder the builder
     */
    public GlobalPlugin(final GatewayContextBuilder builder) {
        this.builder = builder;
    }
    
    @Override
    public Mono<Void> execute(final ServerWebExchange exchange, final GatewayPluginChain chain) {
        final ServerHttpRequest request = exchange.getRequest();
        final HttpHeaders headers = request.getHeaders();
        final String upgrade = headers.getFirst("Upgrade");
        GatewayContext gatewayContext;
        if (!StringUtils.hasText(upgrade) || !"websocket".equals(upgrade)) {
        	gatewayContext = builder.build(exchange);
        } else {
            final MultiValueMap<String, String> queryParams = request.getQueryParams();
            gatewayContext = transformMap(queryParams);
        }
        exchange.getAttributes().put(Constants.CONTEXT, gatewayContext);
        return chain.execute(exchange);
    }
    
    @Override
    public int getOrder() {
        return 0;
    }
    
    private GatewayContext transformMap(final MultiValueMap<String, String> queryParams) {
        GatewayContext gatewayContext = new GatewayContext();
        gatewayContext.setModule(queryParams.getFirst(Constants.MODULE));
        gatewayContext.setMethod(queryParams.getFirst(Constants.METHOD));
        gatewayContext.setRpcType(queryParams.getFirst(Constants.RPC_TYPE));
        return gatewayContext;
    }
    
    @Override
    public String named() {
        return "global";
    }
}
