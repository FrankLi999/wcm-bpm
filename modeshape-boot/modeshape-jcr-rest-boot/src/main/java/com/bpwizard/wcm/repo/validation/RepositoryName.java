package com.bpwizard.wcm.repo.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Documented
@Constraint(validatedBy = { })
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@NotBlank(message = "Repository name is mandatory")
@Size(max=12, message = "Repository name must be no more than 12 characters")
public @interface RepositoryName {
    String message() default "Invalid repository name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default { }; 
}