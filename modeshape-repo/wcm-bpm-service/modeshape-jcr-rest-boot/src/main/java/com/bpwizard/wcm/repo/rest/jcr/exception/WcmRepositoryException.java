package com.bpwizard.wcm.repo.rest.jcr.exception;

import javax.jcr.RepositoryException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WcmRepositoryException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private RepositoryException exception;
	public WcmRepositoryException(RepositoryException exception) {
		this.exception = exception;
	}
	
	public RepositoryException getRepositoryException() {
		return this.exception;
	}
}
