package com.bpwizard.gateway.plugin.base.condition.judge;

import com.bpwizard.gateway.common.dto.ConditionData;

/**
 * this is operator Judge.

 */
@FunctionalInterface
public interface OperatorJudge {

    /**
     * judge conditionData and realData is match.
     *
     * @param conditionData {@linkplain ConditionData}
     * @param realData       realData
     * @return true is pass  false is not pass.
     */
    Boolean judge(ConditionData conditionData, String realData);

}
