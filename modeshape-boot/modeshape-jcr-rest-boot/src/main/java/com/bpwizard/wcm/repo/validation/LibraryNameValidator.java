package com.bpwizard.wcm.repo.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;

import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.jcr.model.Library;

public class LibraryNameValidator implements ConstraintValidator<LibraryName, Library> {
	private String message = "";
	private String[] propertyNames;
	public void initialize(LibraryName constraintAnnotation) {
		this.message = constraintAnnotation.message();
		this.propertyNames = constraintAnnotation.propertyNames();
    }
	
	@Override
	public boolean isValid(Library library, ConstraintValidatorContext context) {
		String language = library.getLanguage();
		String name = library.getName();
		boolean valid = true;
		switch (language) {
			case "en":
			case "fr":
				valid = name.length() < 64;
				break;
			case"zh":
				valid = name.length() < 32;
				break;
			default:
				valid = true;
				break;
		}
		
		if (!valid) {
			String message = StringUtils.hasText(this.message) ? this.message : context.getDefaultConstraintMessageTemplate();
			context.disableDefaultConstraintViolation();
	        ConstraintViolationBuilder violationBuilder = context.buildConstraintViolationWithTemplate(message);
	        for (String propertyName : propertyNames) {
	        	NodeBuilderCustomizableContext nbdc = violationBuilder.addPropertyNode(propertyName);
	            nbdc.addConstraintViolation();
	        }
		}
		return valid;
	}

}
