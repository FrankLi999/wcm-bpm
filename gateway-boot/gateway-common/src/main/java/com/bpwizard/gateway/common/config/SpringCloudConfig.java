package com.bpwizard.gateway.common.config;

import lombok.Data;

import java.io.Serializable;

/**
 * The springCloud plugin configuration for eureka.
 */
@Data
public class SpringCloudConfig implements Serializable {

	private static final long serialVersionUID = -9096481643341524528L;

	private String serviceUrl;

    private Integer leaseRenewalIntervalInSeconds;

    private Integer leaseExpirationDurationInSeconds;

}
