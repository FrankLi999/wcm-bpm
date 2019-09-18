package com.bpwizard.wcm.repo.rest.jcr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class WcmSystemException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private Throwable throwable;
	public WcmSystemException(Throwable throwable) {
		this.throwable = throwable;
	}
	
	public Throwable getThrowable() {
		return this.throwable;
	}
}
