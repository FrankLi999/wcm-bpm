package com.bpwizard.wcm.repo.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import javax.validation.Payload;
@Documented
@Constraint(validatedBy = StringValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@NotNull(message = "Value cannot be null")
@ReportAsSingleViolation
public @interface ValidateString {

    String[] acceptedValues();

    String message() default "Invalid String";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default { }; 
}