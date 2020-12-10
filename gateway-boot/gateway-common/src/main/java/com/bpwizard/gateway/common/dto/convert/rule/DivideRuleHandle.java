package com.bpwizard.gateway.common.dto.convert.rule;

import lombok.Data;

import java.io.Serializable;

import com.bpwizard.gateway.common.constant.Constants;

/**
 * The type Divide rule handle.
 */
@Data
public class DivideRuleHandle implements Serializable {
	private static final long serialVersionUID = -2221838527228698615L;

	/**
     * loadBalance.
     * {@linkplain com.bpwizard.gateway.common.enums.LoadBalanceEnum}
     */
    private String loadBalance;

    /**
     * http retry.
     */
    private int retry;

    /**
     * timeout is required.
     */
    private long timeout = Constants.TIME_OUT;

}
