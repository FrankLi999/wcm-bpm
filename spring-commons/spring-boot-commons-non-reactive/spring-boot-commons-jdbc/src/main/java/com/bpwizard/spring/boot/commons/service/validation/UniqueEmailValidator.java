package com.bpwizard.spring.boot.commons.service.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bpwizard.spring.boot.commons.service.domain.UserService;

/**
 * Validator for unique-email
 */
public class UniqueEmailValidator
implements ConstraintValidator<UniqueEmail, String> {

	private static final Logger logger = LoggerFactory.getLogger(UniqueEmailValidator.class);

	private UserService<?, ?> userService;

	public UniqueEmailValidator(UserService<?, ?> userService) {
		
		this.userService = userService;
		logger.info("Created");
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		
		logger.debug("Validating whether email is unique: " + email);
		return !userService.findByEmail(email).isPresent();
	}
}
