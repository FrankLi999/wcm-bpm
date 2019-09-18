package com.bpwizard.spring.boot.commons.webflux.exceptions.handlers;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.bpwizard.spring.boot.commons.exceptions.VersionException;
import com.bpwizard.spring.boot.commons.exceptions.handlers.AbstractExceptionHandler;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class VersionExceptionHandler extends AbstractExceptionHandler<VersionException> {

	public VersionExceptionHandler() {
		
		super(VersionException.class);
		log.info("Created");
	}
	
	@Override
	public HttpStatus getStatus(VersionException ex) {
		return HttpStatus.CONFLICT;
	}
}