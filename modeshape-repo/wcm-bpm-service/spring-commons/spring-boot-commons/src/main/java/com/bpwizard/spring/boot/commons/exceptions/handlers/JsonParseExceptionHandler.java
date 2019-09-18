package com.bpwizard.spring.boot.commons.exceptions.handlers;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.bpwizard.spring.boot.commons.exceptions.handlers.AbstractBadRequestExceptionHandler;
import com.fasterxml.jackson.core.JsonParseException;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class JsonParseExceptionHandler extends AbstractBadRequestExceptionHandler<JsonParseException> {

	public JsonParseExceptionHandler() {
		
		super(JsonParseException.class);
		log.info("Created");
	}
}
