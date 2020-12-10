package com.bpwizard.gateway.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * RuleDTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RuleData implements Serializable {

	private static final long serialVersionUID = 6175096940368680986L;

	private String id;

    private String name;

    private String pluginName;

    private String selectorId;

    /**
     * match way（0 and  1 or).
     */
    private Integer matchMode;

    private Integer sort;

    private Boolean enabled;

    private Boolean loged;

    /**
     * handle message（different plugin have different handle to mark ,json style）.
     */
    private String handle;

    private List<ConditionData> conditionDataList;
}
