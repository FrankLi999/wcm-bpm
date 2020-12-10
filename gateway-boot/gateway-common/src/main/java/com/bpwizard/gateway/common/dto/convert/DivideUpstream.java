package com.bpwizard.gateway.common.dto.convert;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * this is divide upstream.
 */
@Data
@ToString
public class DivideUpstream implements Serializable {

	private static final long serialVersionUID = 507076816512804707L;

	/**
     * host.
     */
    private String upstreamHost;

    /**
     * this is http protocol.
     */
    private String protocol;

    /**
     * url.
     */
    private String upstreamUrl;

    /**
     * weight.
     */
    private int weight;

}
