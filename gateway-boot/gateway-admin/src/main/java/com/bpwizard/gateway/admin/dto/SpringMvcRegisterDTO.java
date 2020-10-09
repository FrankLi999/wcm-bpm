package com.bpwizard.gateway.admin.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * The type Spring mvc register dto.
 */
@Data
public class SpringMvcRegisterDTO implements Serializable {
    
	private static final long serialVersionUID = -24725000659175039L;

	private String appName;
    
    private String context;
    
    private String path;
    
    private String pathDesc;
    
    private String rpcType;
    
    private String host;
    
    private Integer port;
    
    private String ruleName;
    
    private boolean enabled;
}
