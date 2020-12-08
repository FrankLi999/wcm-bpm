package com.bpwizard.spring.boot.commons.web.security;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bpwizard.spring.boot.commons.domain.AbstractAuditorAware;
import com.bpwizard.spring.boot.commons.security.UserDto;
import com.bpwizard.spring.boot.commons.web.util.WebUtils;

/**
 * Needed for auto-filling of the
 * AbstractAuditable columns of AbstractUser
 */
public class SpringWebAuditorAware<ID extends Serializable>
extends AbstractAuditorAware<ID> {
	
    private static final Logger logger = LoggerFactory.getLogger(SpringWebAuditorAware.class);
    
	public SpringWebAuditorAware() {		
		logger.info("Created");
	}

	@Override
	protected UserDto currentUser() {		
		return WebUtils.currentUser();
	}	
}
