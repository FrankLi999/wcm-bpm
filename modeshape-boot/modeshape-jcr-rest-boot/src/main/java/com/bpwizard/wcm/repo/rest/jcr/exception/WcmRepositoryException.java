package com.bpwizard.wcm.repo.rest.jcr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WcmRepositoryException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private WcmError wcmError;
	public WcmRepositoryException(WcmError wcmError) {
		super(wcmError.getMessage());	
		this.wcmError = wcmError;
	}
	public WcmRepositoryException(Throwable throwable, WcmError wcmError) {
		super(throwable);
		this.wcmError = wcmError;
	}
	
	public WcmError getWcmError() {
		return this.wcmError;
	}
}
