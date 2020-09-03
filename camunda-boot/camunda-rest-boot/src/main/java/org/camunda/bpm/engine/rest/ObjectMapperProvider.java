package org.camunda.bpm.engine.rest;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperProvider {
	private static ObjectMapper objectMapper = new ObjectMapper();
	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}
}
