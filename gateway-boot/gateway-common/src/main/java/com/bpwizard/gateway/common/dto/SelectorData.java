package com.bpwizard.gateway.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * SelectorDTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectorData implements Serializable {

	private static final long serialVersionUID = -827259396900651350L;

	private String id;

    private String pluginId;

    /**
     * plugin name.
     */
    private String pluginName;

    private String name;

    /**
     * matchMode（0 and  1 or).
     */
    private Integer matchMode;

    /**
     * type（false full，true custom).
     */
    private Integer type;

    private Integer sort;

    private Boolean enabled;

    private Boolean loged;

    private Boolean continued;

    private String handle;

    private List<ConditionData> conditionList;
}
