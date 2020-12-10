package com.bpwizard.gateway.plugin.base.condition.judge;

import com.bpwizard.gateway.common.dto.ConditionData;

/**
 * this is like impl.
 */
public class LikeOperatorJudge implements OperatorJudge {

    @Override
    public Boolean judge(final ConditionData conditionData, final String realData) {
        return realData.contains(conditionData.getParamValue().trim());
    }
}
