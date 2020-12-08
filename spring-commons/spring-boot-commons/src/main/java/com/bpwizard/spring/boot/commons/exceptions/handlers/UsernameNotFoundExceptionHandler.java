package com.bpwizard.spring.boot.commons.exceptions.handlers;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class UsernameNotFoundExceptionHandler extends AbstractExceptionHandler<UsernameNotFoundException> {
	
	public UsernameNotFoundExceptionHandler() {
		
		super(UsernameNotFoundException.class);
		logger.info("Created");
	}
	
	@Override
	public HttpStatus getStatus(UsernameNotFoundException ex) {
		return HttpStatus.UNAUTHORIZED;
	}
}
