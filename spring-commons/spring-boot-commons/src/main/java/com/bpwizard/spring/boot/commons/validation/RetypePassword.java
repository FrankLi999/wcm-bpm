package com.bpwizard.spring.boot.commons.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;

/**
 * Annotation for retype password constraint
 */
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=RetypePasswordValidator.class)
public @interface RetypePassword {
 
    String message() default "{com.bpwizard.spring.different.passwords}";

    Class[] groups() default {};
    
    Class[] payload() default {};
}
