package com.bpwizard.spring.boot.commons.exceptions.handlers;

import java.util.Collection;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.bpwizard.spring.boot.commons.exceptions.SpringFieldError;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class WebExchangeBindExceptionHandler extends AbstractValidationExceptionHandler<WebExchangeBindException> {

	public WebExchangeBindExceptionHandler() {
		
		super(WebExchangeBindException.class);
		log.info("Created");
	}
	
	@Override
	public Collection<SpringFieldError> getErrors(WebExchangeBindException ex) {
		return SpringFieldError.getErrors(ex);
	}
}
