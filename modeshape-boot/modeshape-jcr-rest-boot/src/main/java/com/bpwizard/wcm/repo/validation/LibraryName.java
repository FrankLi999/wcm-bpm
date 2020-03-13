package com.bpwizard.wcm.repo.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = { LibraryNameValidator.class })
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LibraryName {
	String[] propertyNames();
	String message() default "Library Name can not longer than 64 character when language is en or fr, can not be longer than 32 when language is zh" ;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
