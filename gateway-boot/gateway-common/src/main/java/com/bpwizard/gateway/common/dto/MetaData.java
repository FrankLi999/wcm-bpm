package com.bpwizard.gateway.common.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class MetaData implements Serializable {
    
	private static final long serialVersionUID = -1285349013587064491L;

	private String id;
    
    private String appName;
    
    private String contextPath;
    
    private String path;
    
    private String rpcType;
    
    private String serviceName;
    
    private String methodName;
    
    private String parameterTypes;
    
    private String rpcExt;
    
    private Boolean enabled;
}
