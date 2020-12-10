package com.bpwizard.gateway.plugin.base.utils;

import com.bpwizard.gateway.common.dto.ConditionData;
import com.bpwizard.gateway.common.enums.MatchModeEnum;
import com.bpwizard.gateway.spi.ExtensionLoader;
import org.springframework.web.server.ServerWebExchange;

import com.bpwizard.gateway.plugin.base.condition.strategy.MatchStrategy;

import java.util.List;

/**
 * MatchStrategyFactory.
 */
public class MatchStrategyUtils {

    /**
     * Match boolean.
     *
     * @param strategy          the strategy
     * @param conditionDataList the condition data list
     * @param exchange          the exchange
     * @return the boolean
     */
    public static boolean match(final Integer strategy, final List<ConditionData> conditionDataList, final ServerWebExchange exchange) {
        String matchMode = MatchModeEnum.getMatchModeByCode(strategy);
        MatchStrategy matchStrategy = ExtensionLoader.getExtensionLoader(MatchStrategy.class).getJoin(matchMode);
        return matchStrategy.match(conditionDataList, exchange);
    }
}
