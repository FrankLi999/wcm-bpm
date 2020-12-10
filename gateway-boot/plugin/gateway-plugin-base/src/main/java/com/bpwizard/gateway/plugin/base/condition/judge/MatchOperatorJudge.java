package com.bpwizard.gateway.plugin.base.condition.judge;

import com.bpwizard.gateway.common.dto.ConditionData;
import com.bpwizard.gateway.common.enums.ParamTypeEnum;
import com.bpwizard.gateway.common.utils.PathMatchUtils;

import java.util.Objects;

/**
 * this is match impl.
 */
public class MatchOperatorJudge implements OperatorJudge {

    @Override
    public Boolean judge(final ConditionData conditionData, final String realData) {
        if (Objects.equals(ParamTypeEnum.URI.getName(), conditionData.getParamType())) {
            return PathMatchUtils.match(conditionData.getParamValue().trim(), realData);
        }
        return realData.contains(conditionData.getParamValue().trim());
    }
}
