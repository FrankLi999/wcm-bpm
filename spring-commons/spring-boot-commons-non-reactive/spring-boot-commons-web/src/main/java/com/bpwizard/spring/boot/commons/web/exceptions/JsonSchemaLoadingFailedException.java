package com.bpwizard.spring.boot.commons.web.exceptions;

public class JsonSchemaLoadingFailedException extends RuntimeException {

	private static final long serialVersionUID = 451970424962580488L;

	public JsonSchemaLoadingFailedException(String message) {
        super(message);
    }

    public JsonSchemaLoadingFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}