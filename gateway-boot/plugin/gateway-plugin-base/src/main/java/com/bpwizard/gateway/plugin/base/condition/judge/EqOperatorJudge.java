package com.bpwizard.gateway.plugin.base.condition.judge;

import com.bpwizard.gateway.common.dto.ConditionData;

import java.util.Objects;

/**
 * this is eq impl.
 */
public class EqOperatorJudge implements OperatorJudge {

    @Override
    public Boolean judge(final ConditionData conditionData, final String realData) {
        return Objects.equals(realData, conditionData.getParamValue().trim());
    }
}
