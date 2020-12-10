package com.bpwizard.gateway.plugin.base.condition.strategy;

import com.bpwizard.gateway.common.dto.ConditionData;
import com.bpwizard.gateway.spi.SPI;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

/**
 * This is condition strategy.
 */
@SPI
public interface MatchStrategy {

    /**
     * this is condition match.
     *
     * @param conditionDataList condition list.
     * @param exchange          {@linkplain ServerWebExchange}
     * @return true is match , false is not match.
     */
    Boolean match(List<ConditionData> conditionDataList, ServerWebExchange exchange);
}
