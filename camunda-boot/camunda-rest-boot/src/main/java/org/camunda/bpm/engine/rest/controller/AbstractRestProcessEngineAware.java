package org.camunda.bpm.engine.rest.controller;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.ObjectMapperProvider;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractRestProcessEngineAware {
	@Autowired
	protected ProcessEngine processEngine;
	protected String rootResourcePath = "http://localhost:28080";
	
	public ObjectMapper getObjectMapper() {
		return ObjectMapperProvider.getObjectMapper();
	}
	
	public ProcessEngine getProcessEngine() {
		return this.processEngine;
	}
}
