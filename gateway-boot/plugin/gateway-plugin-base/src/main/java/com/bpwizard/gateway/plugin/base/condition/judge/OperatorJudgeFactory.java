package com.bpwizard.gateway.plugin.base.condition.judge;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.util.StringUtils;

import com.bpwizard.gateway.common.dto.ConditionData;
import com.bpwizard.gateway.common.enums.OperatorEnum;

/**
 * ConditionJudge.
 */
public class OperatorJudgeFactory {

    private static final Map<String, OperatorJudge> OPERATOR_JUDGE_MAP = new HashMap<>(4);

    static {
        OPERATOR_JUDGE_MAP.put(OperatorEnum.EQ.getAlias(), new EqOperatorJudge());
        OPERATOR_JUDGE_MAP.put(OperatorEnum.MATCH.getAlias(), new MatchOperatorJudge());
        OPERATOR_JUDGE_MAP.put(OperatorEnum.LIKE.getAlias(), new LikeOperatorJudge());
        OPERATOR_JUDGE_MAP.put(OperatorEnum.REGEX.getAlias(), new RegExOperatorJudge());
    }

    /**
     * judge request realData has by pass.
     * @param conditionData condition data
     * @param realData       realData
     * @return is true pass   is false not pass
     */
    public static Boolean judge(final ConditionData conditionData, final String realData) {
        if (Objects.isNull(conditionData) || !StringUtils.hasText(realData)) {
            return false;
        }
        return OPERATOR_JUDGE_MAP.get(conditionData.getOperator()).judge(conditionData, realData);
    }
}
