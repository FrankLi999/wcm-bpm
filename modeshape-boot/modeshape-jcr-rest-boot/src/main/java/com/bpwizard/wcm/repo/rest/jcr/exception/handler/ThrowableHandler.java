package com.bpwizard.wcm.repo.rest.jcr.exception.handler;

import java.util.Collection;
import java.util.Collections;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;

import com.bpwizard.spring.boot.commons.exceptions.SpringFieldError;
import com.bpwizard.spring.boot.commons.exceptions.handlers.AbstractValidationExceptionHandler;

// @Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ThrowableHandler extends AbstractValidationExceptionHandler<Throwable> {

	public ThrowableHandler() {
		
		super(Throwable.class);
		logger.info("Created");
	}
	
	@Override
	public Collection<SpringFieldError> getErrors(Throwable ex) {
		return Collections.emptyList();
	}
	
	@Override
	public HttpStatus getStatus(Throwable ex) {
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
	
	@Override
	public String getMessage(Throwable ex) {
		return "Unexpected server error";
	}
}