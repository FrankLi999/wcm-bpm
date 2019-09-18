package com.bpwizard.spring.boot.commons.reactive.service.validation;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.bpwizard.spring.boot.commons.reactive.service.SpringReactiveService;

/**
 * Validator for unique-email
 * 
 * @author Sanjay Patel
 */
public class UniqueEmailValidator
implements ConstraintValidator<UniqueEmail, String> {

	private static final Logger log = LogManager.getLogger(UniqueEmailValidator.class);

	private MongoTemplate mongoTemplate;
	private Class<?> userClass;

	public UniqueEmailValidator(MongoTemplate mongoTemplate,
			SpringReactiveService<?,?> springReactiveService) {
		
		this.mongoTemplate = mongoTemplate;
		this.userClass = springReactiveService.newUser().getClass();
		log.info("Created");
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		
		log.debug("Validating whether email is unique: " + email);
		return !mongoTemplate.exists(query(where("email").is(email)), userClass);
	}
}
