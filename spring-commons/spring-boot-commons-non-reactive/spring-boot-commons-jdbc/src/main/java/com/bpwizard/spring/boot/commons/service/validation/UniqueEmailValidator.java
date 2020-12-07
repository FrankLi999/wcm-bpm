package com.bpwizard.spring.boot.commons.service.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bpwizard.spring.boot.commons.service.domain.AbstractUserService;

/**
 * Validator for unique-email
 */
public class UniqueEmailValidator
implements ConstraintValidator<UniqueEmail, String> {

	private static final Logger log = LogManager.getLogger(UniqueEmailValidator.class);

	private AbstractUserService<?,?> userService;

	public UniqueEmailValidator(AbstractUserService<?, ?> userService) {
		
		this.userService = userService;
		log.info("Created");
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		
		log.debug("Validating whether email is unique: " + email);
		return !userService.findByEmail(email).isPresent();
	}
}
