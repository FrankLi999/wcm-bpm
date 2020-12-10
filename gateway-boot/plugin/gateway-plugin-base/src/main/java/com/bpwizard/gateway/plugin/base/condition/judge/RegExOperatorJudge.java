package com.bpwizard.gateway.plugin.base.condition.judge;

import com.bpwizard.gateway.common.dto.ConditionData;

import java.util.regex.Pattern;

/**
 * The type Reg ex operator judge.
 */
public class RegExOperatorJudge implements OperatorJudge {

    @Override
    public Boolean judge(final ConditionData conditionData, final String realData) {
        return Pattern.matches(conditionData.getParamValue(), realData);
    }
}
