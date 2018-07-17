package com.bpwizard.myresources.api.exception;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.ToString;

// @JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString
public class FieldErrorResource implements Serializable {
    private static final long serialVersionUID = 4824631164888778494L;
	private String objectName;
    private String field;
    private String code;
    private String message;

    @JsonCreator
    public FieldErrorResource(String objectName, String field, String code, String message) {

        this.objectName = objectName;
        this.field = field;
        this.code = code;
        this.message = message;
    }
}
