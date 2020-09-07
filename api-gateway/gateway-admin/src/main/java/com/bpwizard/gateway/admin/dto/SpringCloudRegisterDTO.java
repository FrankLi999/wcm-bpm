package com.bpwizard.gateway.admin.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * The type Spring cloud register dto.
 */
@Data
public class SpringCloudRegisterDTO implements Serializable {
    
	private static final long serialVersionUID = 6281481752269229804L;

	private String appName;
    
    private String context;
    
    private String path;
    
    private String pathDesc;
    
    private String rpcType;
    
    private String ruleName;
    
    private boolean enabled;
}
