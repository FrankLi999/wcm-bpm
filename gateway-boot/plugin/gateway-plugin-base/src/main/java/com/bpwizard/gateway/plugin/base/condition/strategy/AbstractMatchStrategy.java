package com.bpwizard.gateway.plugin.base.condition.strategy;

import com.bpwizard.gateway.common.constant.Constants;
import com.bpwizard.gateway.common.dto.ConditionData;
import com.bpwizard.gateway.common.enums.ParamTypeEnum;
import com.bpwizard.gateway.common.utils.ReflectUtils;
import com.bpwizard.gateway.plugin.api.context.GatewayContext;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import com.bpwizard.gateway.plugin.base.utils.HostAddressUtils;

import java.util.List;
import java.util.Objects;

/**
 * AbstractMatchStrategy.
 */
abstract class AbstractMatchStrategy {

    /**
     * Build real data string.
     *
     * @param condition the condition
     * @param exchange  the exchange
     * @return the string
     */
    String buildRealData(final ConditionData condition, final ServerWebExchange exchange) {
        String realData = "";
        ParamTypeEnum paramTypeEnum = ParamTypeEnum.getParamTypeEnumByName(condition.getParamType());
        switch (paramTypeEnum) {
            case HEADER:
                final HttpHeaders headers = exchange.getRequest().getHeaders();
                final List<String> list = headers.get(condition.getParamName());
                if (CollectionUtils.isEmpty(list)) {
                    return realData;
                }
                realData = Objects.requireNonNull(headers.get(condition.getParamName())).stream().findFirst().orElse("");
                break;
            case URI:
                realData = exchange.getRequest().getURI().getPath();
                break;
            case QUERY:
                final MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();
                realData = queryParams.getFirst(condition.getParamName());
                break;
            case HOST:
                realData = HostAddressUtils.acquireHost(exchange);
                break;
            case IP:
                realData = HostAddressUtils.acquireIp(exchange);
                break;
            case POST:
                final GatewayContext gatewayContext = exchange.getAttribute(Constants.CONTEXT);
                realData = (String) ReflectUtils.getFieldValue(gatewayContext, condition.getParamName());
                break;
            default:
                break;
        }
        return realData;
    }

}
