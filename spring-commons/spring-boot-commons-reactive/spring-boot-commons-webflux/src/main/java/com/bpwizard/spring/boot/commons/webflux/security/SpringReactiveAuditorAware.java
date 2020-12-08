package com.bpwizard.spring.boot.commons.webflux.security;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bpwizard.spring.boot.commons.domain.AbstractAuditorAware;
import com.bpwizard.spring.boot.commons.security.UserDto;

/**
 * Needed for auto-filling of the
 * AbstractAuditable columns of AbstractUser
 */
public class SpringReactiveAuditorAware<ID extends Serializable>
extends AbstractAuditorAware<ID> {
	
    private static final Logger logger = LoggerFactory.getLogger(SpringReactiveAuditorAware.class);
    
	public SpringReactiveAuditorAware() {		
		logger.info("Created");
	}

	@Override
	protected UserDto currentUser() {
		
		// TODO: Can't return a mono, as below
		// So, sorry, no reactive auditing until Spring Data supports it
		// return LecrUtils.currentUser();
		
		return null;
	}	
}
