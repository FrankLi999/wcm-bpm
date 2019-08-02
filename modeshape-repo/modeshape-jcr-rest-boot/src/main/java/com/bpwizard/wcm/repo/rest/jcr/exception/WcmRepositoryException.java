package com.bpwizard.wcm.repo.rest.jcr.exception;

import javax.jcr.RepositoryException;

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
