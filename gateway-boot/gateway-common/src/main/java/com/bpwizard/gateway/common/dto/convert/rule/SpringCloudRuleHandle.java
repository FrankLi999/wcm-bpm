package com.bpwizard.gateway.common.dto.convert.rule;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

import com.bpwizard.gateway.common.constant.Constants;

/**
 * The type Spring cloud rule handle.
 */
@ToString
@Getter
@Setter
public class SpringCloudRuleHandle implements Serializable {

	private static final long serialVersionUID = -902116766513506454L;

	/**
     * this remote uri path.
     */
    private String path;

    /**
     * timeout is required.
     */
    private long timeout = Constants.TIME_OUT;
}
