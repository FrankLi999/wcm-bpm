package com.bpwizard.myresources.api.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Getter;
import lombok.ToString;

// @JsonSerialize(using = ErrorResourceSerializer.class)
// @JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString
@JsonRootName("errors")
public class ErrorResource implements Serializable {
	private static final long serialVersionUID = -793195754546345910L;
	private List<FieldErrorResource> fieldErrors; // = new ArrayList<>();
    private String code;
    private String message;
    
//    public ErrorResource(List<FieldErrorResource> fieldErrorResources) {
//        this.fieldErrors = fieldErrorResources;
//    }
    
    @JsonCreator
    public ErrorResource(String code, String message, List<FieldErrorResource> fieldErrors) {
        this.code = code;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

//    public void add(String objectName, String path, String code, String message) {
//        this.fieldErrors.add(new FieldErrorResource(objectName, path, code, message));
//    }
}
