package com.bpwizard.gateway.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * Data set, including {@link AppAuthData}、{@link ConditionData}、{@link PluginData}、{@link RuleData}、{@link SelectorData}.
 *
 * @param <T> the type parameter
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class WebsocketData<T> implements Serializable {

	private static final long serialVersionUID = -6698735653002078993L;

	/**
     * group type.
     * {@linkplain com.bpwizard.gateway.common.enums.ConfigGroupEnum}
     */
    private String groupType;

    /**
     * event type.
     * {@linkplain com.bpwizard.gateway.common.enums.DataEventTypeEnum}
     */
    private String eventType;

    /**
     * data list.
     * {@link AppAuthData}、{@link ConditionData}、{@link PluginData}、{@link RuleData}、{@link SelectorData}.
     */
    private List<T> data;

}
