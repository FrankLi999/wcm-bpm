package com.bpwizard.spring.boot.commons.webflux.security;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bpwizard.spring.boot.commons.domain.AbstractAuditorAware;
import com.bpwizard.spring.boot.commons.security.UserDto;

/**
 * Needed for auto-filling of the
 * AbstractAuditable columns of AbstractUser
 *  
 * @author Sanjay Patel
 */
public class SpringReactiveAuditorAware<ID extends Serializable>
extends AbstractAuditorAware<ID> {
	
    private static final Logger log = LogManager.getLogger(SpringReactiveAuditorAware.class);
    
	public SpringReactiveAuditorAware() {		
		log.info("Created");
	}

	@Override
	protected UserDto currentUser() {
		
		// TODO: Can't return a mono, as below
		// So, sorry, no reactive auditing until Spring Data supports it
		// return LecrUtils.currentUser();
		
		return null;
	}	
}
