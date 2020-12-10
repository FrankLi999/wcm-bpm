package com.bpwizard.gateway.common.dto;

import java.io.Serializable;
import java.util.List;

import com.bpwizard.gateway.common.utils.JsonUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * Data set, including {@link AppAuthData}、{@link ConditionData}、{@link PluginData}、{@link RuleData}、{@link SelectorData}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ConfigData<T> implements Serializable {

	private static final long serialVersionUID = 7865259836897134643L;

	private String md5;

    private long lastModifyTime;

    private List<T> data;

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
