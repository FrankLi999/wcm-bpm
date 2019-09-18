package org.camunda.bpm.engine.rest.controller;

import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractRestProcessEngineAware {
	@Autowired
	protected ProcessEngine processEngine;
	protected ObjectMapper objectMapper = new ObjectMapper();
	protected String rootResourcePath = "http://localhost:8080";
	
	protected ObjectMapper getObjectMapper() {
		return this.objectMapper;
	}
	
	protected ProcessEngine getProcessEngine() {
		return this.processEngine;
	}
}
