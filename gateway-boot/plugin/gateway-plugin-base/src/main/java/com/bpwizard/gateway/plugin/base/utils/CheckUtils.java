package com.bpwizard.gateway.plugin.base.utils;

import com.bpwizard.gateway.common.enums.PluginEnum;
import com.bpwizard.gateway.plugin.api.result.GatewayResultEnum;
import com.bpwizard.gateway.plugin.api.GatewayPluginChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * The type Selector and rule check utils.
 */
public class CheckUtils {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckUtils.class);

    /**
     * Check selector mono.
     *
     * @param pluginName the plugin name
     * @param exchange   the exchange
     * @param chain      the chain
     * @return the mono
     */
    public static Mono<Void> checkSelector(final String pluginName, final ServerWebExchange exchange, final GatewayPluginChain chain) {
        if (PluginEnum.DIVIDE.getName().equals(pluginName)
                // || PluginEnum.DUBBO.getName().equals(pluginName)
                || PluginEnum.SPRING_CLOUD.getName().equals(pluginName)) {
            LOGGER.error("can not match selector data :{}", pluginName);
            Object error = GatewayResultWrap.error(GatewayResultEnum.CANNOT_FIND_SELECTOR.getCode(), GatewayResultEnum.CANNOT_FIND_SELECTOR.getMsg(), null);
            return WebFluxResultUtils.result(exchange, error);
        }
        return chain.execute(exchange);
    }

    /**
     * Check rule mono.
     *
     * @param pluginName the plugin name
     * @param exchange   the exchange
     * @param chain      the chain
     * @return the mono
     */
    public static Mono<Void> checkRule(final String pluginName, final ServerWebExchange exchange, final GatewayPluginChain chain) {
        if (PluginEnum.DIVIDE.getName().equals(pluginName)
                // || PluginEnum.DUBBO.getName().equals(pluginName)
                || PluginEnum.SPRING_CLOUD.getName().equals(pluginName)) {
            LOGGER.error("can not match rule data :{}", pluginName);
            Object error = GatewayResultWrap.error(GatewayResultEnum.RULE_NOT_FIND.getCode(), GatewayResultEnum.RULE_NOT_FIND.getMsg(), null);
            return WebFluxResultUtils.result(exchange, error);
        }
        return chain.execute(exchange);
    }
}
