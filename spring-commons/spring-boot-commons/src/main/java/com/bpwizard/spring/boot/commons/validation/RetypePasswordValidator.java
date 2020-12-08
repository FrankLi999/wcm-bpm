package com.bpwizard.spring.boot.commons.validation;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validator for RetypePassword constraint
 * 
 * @see <a href="http://docs.jboss.org/hibernate/validator/4.1/reference/en-US/html/validator-usingvalidator.html#d0e326">Hibernate Validator</a>
 * @see <a href="http://docs.jboss.org/hibernate/validator/4.1/reference/en-US/html/validator-customconstraints.html#validator-customconstraints-validator">Custom constraints</a>
 */
public class RetypePasswordValidator implements ConstraintValidator<RetypePassword, RetypePasswordForm> {
	
	private static final Logger logger = LoggerFactory.getLogger(RetypePasswordValidator.class);

	@Override
	public boolean isValid(RetypePasswordForm retypePasswordForm,
		ConstraintValidatorContext context) {
		
		if (!Objects.equals(retypePasswordForm.getPassword(),
				            retypePasswordForm.getRetypePassword())) {
			
			logger.debug("Retype password validation failed.");
			
			// Moving the error from form-level to
			// field-level properties: password, retypePassword
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
						"{com.bpwizard.spring.different.passwords}")
						.addPropertyNode("password").addConstraintViolation()
				   .buildConstraintViolationWithTemplate(
						"{com.bpwizard.spring.different.passwords}")
						.addPropertyNode("retypePassword").addConstraintViolation();
			
			return false;	
		}
		
		logger.debug("Retype password validation succeeded.");		
		return true;
	}
}
