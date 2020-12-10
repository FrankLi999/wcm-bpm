package com.bpwizard.gateway.plugin.global;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import com.bpwizard.gateway.common.constant.Constants;
import com.bpwizard.gateway.common.dto.MetaData;
import com.bpwizard.gateway.common.enums.RpcTypeEnum;
import com.bpwizard.gateway.plugin.global.cache.MetaDataCache;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import com.bpwizard.gateway.plugin.api.context.GatewayContext;
import com.bpwizard.gateway.plugin.api.context.GatewayContextBuilder;

/**
 * The type Default gateway context builder.
 */
public class DefaultGatewayContextBuilder implements GatewayContextBuilder {
    
    @Override
    public GatewayContext build(final ServerWebExchange exchange) {
        final ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        MetaData metaData = MetaDataCache.getInstance().obtain(path);
        if (Objects.nonNull(metaData) && metaData.getEnabled()) {
            exchange.getAttributes().put(Constants.META_DATA, metaData);
        }
        return transform(request, metaData);
    }
    
    /**
     * ServerHttpRequest transform RequestDTO .
     *
     * @param request {@linkplain ServerHttpRequest}
     * @return RequestDTO request dto
     */
    private GatewayContext transform(final ServerHttpRequest request, final MetaData metaData) {
        final String appKey = request.getHeaders().getFirst(Constants.APP_KEY);
        final String sign = request.getHeaders().getFirst(Constants.SIGN);
        final String timestamp = request.getHeaders().getFirst(Constants.TIMESTAMP);
        GatewayContext gatewayContext = new GatewayContext();
        String path = request.getURI().getPath();
        gatewayContext.setPath(path);
        if (Objects.nonNull(metaData) && metaData.getEnabled()) {
            if (RpcTypeEnum.SPRING_CLOUD.getName().equals(metaData.getRpcType())) {
                setGatewayContextByHttp(gatewayContext, path);
                gatewayContext.setRpcType(metaData.getRpcType());
            } else if (RpcTypeEnum.SOFA.getName().equals(metaData.getRpcType())) {
                setGatewayContextBySofa(gatewayContext, metaData);
            } else {
                setGatewayContextByHttp(gatewayContext, path);
                gatewayContext.setRpcType(RpcTypeEnum.HTTP.getName());
            }
        } else {
            setGatewayContextByHttp(gatewayContext, path);
            gatewayContext.setRpcType(RpcTypeEnum.HTTP.getName());
        }
        gatewayContext.setAppKey(appKey);
        gatewayContext.setSign(sign);
        gatewayContext.setTimestamp(timestamp);
        gatewayContext.setStartDateTime(LocalDateTime.now());
        Optional.ofNullable(request.getMethod()).ifPresent(httpMethod -> gatewayContext.setHttpMethod(httpMethod.name()));
        return gatewayContext;
    }
    
    private void setGatewayContextBySofa(final GatewayContext gatewayContext, final MetaData metaData) {
        gatewayContext.setModule(metaData.getAppName());
        gatewayContext.setMethod(metaData.getServiceName());
        gatewayContext.setRpcType(metaData.getRpcType());
        gatewayContext.setContextPath(metaData.getContextPath());
    }
    
    private void setGatewayContextByHttp(final GatewayContext gatewayContext, final String path) {
        String contextPath = "/";
        String[] splitList = path.split("/");
        if (splitList.length != 0) {
            contextPath = contextPath.concat(splitList[0]);
        }
        String realUrl = path.substring(contextPath.length());
        gatewayContext.setContextPath(contextPath);
        gatewayContext.setModule(contextPath);
        gatewayContext.setMethod(realUrl);
        gatewayContext.setRealUrl(realUrl);
    }
}
