package com.bpwizard.bpm.demo.batch.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

class JacksonJobHandlerConfigurationSerializer<T extends JacksonJobHandlerConfiguration> {

	private final ObjectMapper mapper;
	private final Class<T> clazz;

	JacksonJobHandlerConfigurationSerializer(final ObjectMapper mapper, final Class<T> clazz) {
		this.mapper = mapper;
		this.clazz = clazz;
	}

	String toCanonicalString(final T instance) {
		try {
			return mapper.writeValueAsString(instance);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException(
					"Error during serialization if job handler configuration of type " + clazz.getName(), e);
		}
	}

	T fromCanonicalString(final String canonicalString) {
		try {
			return mapper.readValue(canonicalString, clazz);
		} catch (IOException e) {
			throw new IllegalArgumentException("Error during de-serialization of " + canonicalString
					+ " to job handler configuration of type " + clazz.getName(), e);
		}
	}
}
