package com.bpwizard.spring.boot.commons.service.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bpwizard.spring.boot.commons.service.domain.AbstractUserRepository;

/**
 * Validator for unique-email
 * 
 * @author Sanjay Patel
 */
public class UniqueEmailValidator
implements ConstraintValidator<UniqueEmail, String> {

	private static final Logger log = LogManager.getLogger(UniqueEmailValidator.class);

	private AbstractUserRepository<?,?> userRepository;

	public UniqueEmailValidator(AbstractUserRepository<?, ?> userRepository) {
		
		this.userRepository = userRepository;
		log.info("Created");
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		
		log.debug("Validating whether email is unique: " + email);
		return !userRepository.findByEmail(email).isPresent();
	}
}
