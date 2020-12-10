package com.bpwizard.gateway.plugin.base.condition.strategy;

import com.bpwizard.gateway.common.dto.ConditionData;
import com.bpwizard.gateway.spi.Join;
import org.springframework.web.server.ServerWebExchange;

import com.bpwizard.gateway.plugin.base.condition.judge.OperatorJudgeFactory;

import java.util.List;

/**
 * This is or match strategy.
 */
@Join
public class OrMatchStrategy extends AbstractMatchStrategy implements MatchStrategy {

    @Override
    public Boolean match(final List<ConditionData> conditionDataList, final ServerWebExchange exchange) {
        return conditionDataList
                .stream()
                .anyMatch(condition -> OperatorJudgeFactory.judge(condition, buildRealData(condition, exchange)));
    }
}
