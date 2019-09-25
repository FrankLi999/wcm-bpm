package com.bpwizard.wcm.repo.rest.jcr.exception;

// @ResponseStatus(HttpStatus.BAD_REQUEST)
public class WcmRepositoryException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public WcmRepositoryException(String message) {
	   super(message);	
	}
	public WcmRepositoryException(Throwable throwable) {
		super(throwable);
	}
}
