package com.bpwizard.wcm.repo.rest.jcr.exception.handler;

import java.util.Collection;
import java.util.Collections;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.bpwizard.spring.boot.commons.exceptions.SpringFieldError;
import com.bpwizard.spring.boot.commons.exceptions.handlers.AbstractBadRequestExceptionHandler;
import com.bpwizard.wcm.repo.rest.jcr.exception.WcmRepositoryException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WcmRepositoryExceptionHandler extends AbstractBadRequestExceptionHandler<WcmRepositoryException> {

	public WcmRepositoryExceptionHandler() {		
		super(WcmRepositoryException.class);
		logger.info("Created");
	}
	
	@Override
	public Collection<SpringFieldError> getErrors(WcmRepositoryException ex) {
		return Collections.emptyList();
	}
	
	@Override
	public String getMessage(WcmRepositoryException ex) {
		return (ex.getWcmError() != null)? ex.getWcmError().getMessage() : null;
	}
	
	@Override
	public String getErrorCode(WcmRepositoryException ex) {
		return (ex.getWcmError() != null)? ex.getWcmError().getErrorCode() : null;
	}
	
	@Override
	public String[] getArguments(WcmRepositoryException ex) {
		return (ex.getWcmError() != null)? ex.getWcmError().getArguments() : null;
	}
}