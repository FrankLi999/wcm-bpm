package com.bpwizard.gateway.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ConditionDTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConditionData implements Serializable {

	private static final long serialVersionUID = -4209120113143649287L;

	/**
     * param type (post  query  uri).
     */
    private String paramType;

    /**
     * {@linkplain com.bpwizard.gateway.common.enums.OperatorEnum}.
     */
    private String operator;

    /**
     * param name.
     */
    private String paramName;

    /**
     * param value.
     */
    private String paramValue;
}
