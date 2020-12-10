package com.bpwizard.gateway.common.dto.convert.selector;

import lombok.Data;

import java.io.Serializable;

/**
 * The type Spring cloud selector handle.
 */
@Data
public class SpringCloudSelectorHandle implements Serializable {

	private static final long serialVersionUID = -5179924463430585823L;
	/**
     * this is register eureka serviceId.
     */
    private String serviceId;
}
