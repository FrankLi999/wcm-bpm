package com.bpwizard.wcm.repo.rest.jcr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WcmRepositoryException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public WcmRepositoryException(String message) {
	   super(message);	
	}
	public WcmRepositoryException(Throwable throwable) {
		super(throwable);
	}
}
