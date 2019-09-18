package com.bpwizard.spring.boot.commons.web.security;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bpwizard.spring.boot.commons.domain.AbstractAuditorAware;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.web.util.WebUtils;

/**
 * Needed for auto-filling of the
 * AbstractAuditable columns of AbstractUser
 *  
 * @author Sanjay Patel
 */
public class SpringWebAuditorAware<ID extends Serializable>
extends AbstractAuditorAware<ID> {
	
    private static final Logger log = LogManager.getLogger(SpringWebAuditorAware.class);
    
	public SpringWebAuditorAware() {		
		log.info("Created");
	}

	@Override
	protected UserDto currentUser() {		
		return WebUtils.currentUser();
	}	
}
