package com.bpwizard.spring.boot.commons.exceptions.handlers;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;

import com.bpwizard.spring.boot.commons.exceptions.util.SpringExceptionUtils;

/**
 * Extend this for any exception handler that should return a 400 response
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public abstract class AbstractValidationExceptionHandler<T extends Throwable> extends AbstractExceptionHandler<T> {

	public AbstractValidationExceptionHandler(Class<?> exceptionClass) {
		super(exceptionClass);
	}

	@Override
	public HttpStatus getStatus(T ex) {
		return HttpStatus.UNPROCESSABLE_ENTITY;
	}
	
	@Override
	public String getMessage(T ex) {
		return SpringExceptionUtils.getMessage("com.bpwizard.spring.validationError");
	}
}
