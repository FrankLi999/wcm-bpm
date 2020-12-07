package com.bpwizard.spring.boot.commons.service.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.bpwizard.spring.boot.commons.util.UserUtils;

/**
 * Annotation for unique-email constraint,
 * ensuring that the given email id is not already
 * used by a user.  
 */
@NotBlank(message = "{com.bpwizard.spring.blank.email}")
@Size(min=UserUtils.EMAIL_MIN, max=UserUtils.EMAIL_MAX,
	message = "{com.bpwizard.spring.invalid.email.size}")
@Email(message = "{com.bpwizard.spring.invalid.email}")
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=UniqueEmailValidator.class)
public @interface UniqueEmail {
 
    String message() default "{com.bpwizard.spring.duplicate.email}";

    @SuppressWarnings("rawtypes")
	Class[] groups() default {};
    
    @SuppressWarnings("rawtypes")
	Class[] payload() default {};
}
