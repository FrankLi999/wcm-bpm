package com.bpwizard.gateway.plugin.api.context;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * the gateway context.
 */
@Data
public class GatewayContext implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2149385162690917960L;

	/**
     * is module data.
     */
    private String module;

    /**
     * is method name .
     */
    private String method;

    /**
     * is rpcType data. now we only support "http","dubbo","springCloud","sofa".
     */
    private String rpcType;

    /**
     * httpMethod now we only support "get","post" .
     */
    private String httpMethod;

    /**
     * this is sign .
     */
    private String sign;

    /**
     * timestamp .
     */
    private String timestamp;

    /**
     * appKey .
     */
    private String appKey;

    /**
     * path.
     */
    private String path;
    
    /**
     * the contextPath.
     */
    private String contextPath;

    /**
     * realUrl.
     */
    private String realUrl;

    /**
     * this is dubbo params.
     */
    private String dubboParams;

    /**
     * startDateTime.
     */
    private LocalDateTime startDateTime;
    

}
