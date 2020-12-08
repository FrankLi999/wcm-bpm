package com.bpwizard.spring.boot.commons.exceptions;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.bpwizard.spring.boot.commons.exceptions.handlers.AbstractExceptionHandler;
import com.bpwizard.spring.boot.commons.exceptions.handlers.ConstraintViolationExceptionHandler;
import com.bpwizard.spring.boot.commons.exceptions.handlers.MultiErrorExceptionHandler;
import com.bpwizard.spring.boot.commons.exceptions.handlers.WebExchangeBindExceptionHandler;
import com.bpwizard.spring.boot.commons.exceptions.util.SpringExceptionUtils;

@Configuration
@AutoConfigureBefore({ValidationAutoConfiguration.class})
public class CommonsExceptionsAutoConfiguration {

	private static final Logger log = LoggerFactory.getLogger(CommonsExceptionsAutoConfiguration.class);
	public CommonsExceptionsAutoConfiguration() {
		log.info("Created");
	}
	
	
	/**
	 * Configures ErrorResponseComposer if missing
	 */	
	@Bean
	@ConditionalOnMissingBean(ErrorResponseComposer.class)
	public <T extends Throwable>
	ErrorResponseComposer<T> errorResponseComposer(List<AbstractExceptionHandler<T>> handlers) {
		
        log.info("Configuring ErrorResponseComposer");       
		return new ErrorResponseComposer<T>(handlers);
	}

	
	/**
	 * Configures ExceptionCodeMaker if missing
	 */	
	@Bean
	@ConditionalOnMissingBean(ExceptionIdMaker.class)
	public ExceptionIdMaker exceptionIdMaker() {
		
        log.info("Configuring ExceptionIdMaker");
        return ex -> {
        	
        	if (ex == null)
        		return null;
        	
        	return ex.getClass().getSimpleName();
        };
	}

	
	/**
	 * Configures SpringExceptionUtils
	 */
	@Bean
	public SpringExceptionUtils SpringExceptionUtils(MessageSource messageSource,
			LocalValidatorFactoryBean validator,
			ExceptionIdMaker exceptionIdMaker) {

        log.info("Configuring SpringExceptionUtils");       		
		return new SpringExceptionUtils(messageSource, validator, exceptionIdMaker);
	}
	
	@Bean
	@ConditionalOnMissingBean(ConstraintViolationExceptionHandler.class)
	public ConstraintViolationExceptionHandler constraintViolationExceptionHandler() {
		return new ConstraintViolationExceptionHandler();
	}
	
	@Bean
	@ConditionalOnMissingBean(MultiErrorExceptionHandler.class)
	public MultiErrorExceptionHandler multiErrorExceptionHandler() {
		return new MultiErrorExceptionHandler();
	}
	
	@Bean
	@ConditionalOnMissingBean(WebExchangeBindExceptionHandler.class)
	public WebExchangeBindExceptionHandler WebExchangeBindExceptionHandler() {
		return new WebExchangeBindExceptionHandler();
	}
}
