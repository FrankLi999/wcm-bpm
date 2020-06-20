package com.bpwizard.wcm.repo.rest.jcr.exception.handler;

import java.util.Collection;
import java.util.Collections;

import javax.jcr.nodetype.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.bpwizard.spring.boot.commons.exceptions.SpringFieldError;
import com.bpwizard.spring.boot.commons.exceptions.handlers.AbstractExceptionHandler;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataIntegrityViolationExceptionHandler extends AbstractExceptionHandler<DataIntegrityViolationException> {

	public DataIntegrityViolationExceptionHandler() {		
		super(DataIntegrityViolationException.class);
		log.info("Created");
	}
	
	@Override
	public Collection<SpringFieldError> getErrors(DataIntegrityViolationException ex) {
		return Collections.emptyList();
	}
	
	@Override
	public HttpStatus getStatus(DataIntegrityViolationException ex) {
		if (ex.getCause() instanceof ConstraintViolationException) {
	        return HttpStatus.CONFLICT;
	    }
	    return HttpStatus.INTERNAL_SERVER_ERROR;
	}
	
	@Override
	public String getMessage(DataIntegrityViolationException ex) {
		if (ex.getCause() instanceof ConstraintViolationException) {
	        return "Database error";
	    }
		return ex.getMessage();
	}
}