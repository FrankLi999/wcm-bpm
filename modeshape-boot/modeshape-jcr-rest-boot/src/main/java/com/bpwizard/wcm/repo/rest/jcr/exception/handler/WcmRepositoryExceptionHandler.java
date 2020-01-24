package com.bpwizard.wcm.repo.rest.jcr.exception.handler;

import java.util.Collection;
import java.util.Collections;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.bpwizard.spring.boot.commons.exceptions.SpringFieldError;
import com.bpwizard.spring.boot.commons.exceptions.handlers.AbstractValidationExceptionHandler;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WcmRepositoryExceptionHandler extends AbstractValidationExceptionHandler<WcmRepositoryException> {

	public WcmRepositoryExceptionHandler() {		
		super(WcmRepositoryException.class);
		log.info("Created");
	}
	
	@Override
	public Collection<SpringFieldError> getErrors(WcmRepositoryException ex) {
		return Collections.emptyList();
	}
	
	@Override
	public HttpStatus getStatus(WcmRepositoryException ex) {
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
	
	@Override
	public String getMessage(WcmRepositoryException ex) {
		return ex.getMessage();
	}
}