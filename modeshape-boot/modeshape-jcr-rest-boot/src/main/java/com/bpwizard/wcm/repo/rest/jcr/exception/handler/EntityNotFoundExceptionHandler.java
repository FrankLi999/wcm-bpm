package com.bpwizard.wcm.repo.rest.jcr.exception.handler;

import java.util.Collection;
import java.util.Collections;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.bpwizard.spring.boot.commons.exceptions.SpringFieldError;
import com.bpwizard.spring.boot.commons.exceptions.handlers.AbstractExceptionHandler;
import com.bpwizard.wcm.repo.rest.jcr.exception.EntityNotFoundException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EntityNotFoundExceptionHandler extends AbstractExceptionHandler<EntityNotFoundException> {

	public EntityNotFoundExceptionHandler() {		
		super(EntityNotFoundException.class);
		logger.info("Created");
	}
	
	@Override
	public Collection<SpringFieldError> getErrors(EntityNotFoundException ex) {
		return Collections.emptyList();
	}
	
	@Override
	public HttpStatus getStatus(EntityNotFoundException ex) {
		return HttpStatus.NOT_FOUND;
	}
}