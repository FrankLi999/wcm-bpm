package com.bpwizard.spring.boot.commons.web.exceptions;

import java.util.Set;
import com.networknt.schema.ValidationMessage;

public class JsonValidationFailedException extends RuntimeException {
    private static final long serialVersionUID = -8990911982101863115L;
	private final Set<ValidationMessage> validationMessages;

    public JsonValidationFailedException(Set<ValidationMessage> validationMessages) {
        super("Json validation failed: " + validationMessages);
        this.validationMessages = validationMessages;
    }

    public Set<ValidationMessage> getValidationMessages() {
        return validationMessages;
    }
}