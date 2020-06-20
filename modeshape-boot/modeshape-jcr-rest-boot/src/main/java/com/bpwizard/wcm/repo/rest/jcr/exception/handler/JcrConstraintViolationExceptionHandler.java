package com.bpwizard.wcm.repo.rest.jcr.exception.handler;

import java.util.Collection;
import java.util.Collections;

import javax.jcr.nodetype.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.bpwizard.spring.boot.commons.exceptions.SpringFieldError;
import com.bpwizard.spring.boot.commons.exceptions.handlers.AbstractExceptionHandler;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JcrConstraintViolationExceptionHandler extends AbstractExceptionHandler<ConstraintViolationException> {

	public JcrConstraintViolationExceptionHandler() {		
		super(ConstraintViolationException.class);
		log.info("Created");
	}
	
	@Override
	public Collection<SpringFieldError> getErrors(ConstraintViolationException ex) {
		return Collections.emptyList();
	}
	
	@Override
	public HttpStatus getStatus(ConstraintViolationException ex) {
		return HttpStatus.BAD_REQUEST;
	}
	
	@Override
	public String getMessage(ConstraintViolationException ex) {
		return ex.getMessage();
	}
}