package com.bpwizard.wcm.repo.rest.jcr.exception.handler;

import java.util.Collection;
import java.util.Collections;

import javax.jcr.nodetype.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.bpwizard.spring.boot.commons.exceptions.SpringFieldError;
import com.bpwizard.spring.boot.commons.exceptions.handlers.AbstractBadRequestExceptionHandler;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JcrConstraintViolationExceptionHandler extends AbstractBadRequestExceptionHandler<ConstraintViolationException> {

	public JcrConstraintViolationExceptionHandler() {		
		super(ConstraintViolationException.class);
		logger.info("Created");
	}
	
	@Override
	public Collection<SpringFieldError> getErrors(ConstraintViolationException ex) {
		return Collections.emptyList();
	}
}