package com.bpwizard.spring.boot.commons.exceptions.handlers;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.bpwizard.spring.boot.commons.exceptions.ErrorResponse;
import com.bpwizard.spring.boot.commons.exceptions.SpringFieldError;
import com.bpwizard.spring.boot.commons.exceptions.util.SpringExceptionUtils;

/**
 * Extend this to code an exception handler
 */
public abstract class AbstractExceptionHandler<T extends Throwable> {
	
	protected static Logger logger = LoggerFactory.getLogger(AbstractExceptionHandler.class);
	
	private Class<?> exceptionClass;
	
	public AbstractExceptionHandler(Class<?> exceptionClass) {
		this.exceptionClass = exceptionClass;
	}

	public Class<?> getExceptionClass() {
		return exceptionClass;
	}
	
	protected String getExceptionId(T ex) {
		return SpringExceptionUtils.getExceptionId(ex);
	}

	protected String getMessage(T ex) {
		return ex.getMessage();
	}
	
	protected HttpStatus getStatus(T ex) {
		return null;
	}
	
	protected Collection<SpringFieldError> getErrors(T ex) {
		return null;
	}

	protected String getErrorCode(T ex) {
		return null;
	}
	
	protected String[] getArguments(T ex) {
		return null;
	}
	
	public ErrorResponse getErrorResponse(T ex) {
    	
		ErrorResponse errorResponse = new ErrorResponse();
		
		errorResponse.setExceptionId(getExceptionId(ex));
		errorResponse.setMessage(getMessage(ex));
		
		if (this.getErrorCode(ex) != null) {
			errorResponse.setErrorCode(this.getErrorCode(ex));
			errorResponse.setArguments(this.getArguments(ex));
		}
		
		HttpStatus status = getStatus(ex);
		if (status != null) {
			errorResponse.setStatus(status.value());
			errorResponse.setReasonPhrase(status.getReasonPhrase());
		}
		
		errorResponse.setErrors(getErrors(ex));
		return errorResponse;
	}
}
