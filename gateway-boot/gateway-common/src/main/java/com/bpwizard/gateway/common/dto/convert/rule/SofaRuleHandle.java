package com.bpwizard.gateway.common.dto.convert.rule;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.bpwizard.gateway.common.constant.Constants;

import java.io.Serializable;

/**
 * The type Sofa rule handle.
 */
@Getter
@Setter
@ToString
public class SofaRuleHandle implements Serializable {

	private static final long serialVersionUID = -4130785145669596017L;

	/**
     * retries.
     */
    private Integer retries;

    /**
     * the loadBalance.
     * {@linkplain com.bpwizard.gateway.common.enums.LoadBalanceEnum}
     */
    private String loadBalance;

    /**
     * timeout is required.
     */
    private long timeout = Constants.TIME_OUT;
}
