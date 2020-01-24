package com.bpwizard.bpm.content.listener;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

public class ProcessExecutionListener implements ExecutionListener {

	public void notify(DelegateExecution execution) throws Exception {
		execution.setVariable("variableSetInExecutionListener", "firstValue");
		execution.setVariable("eventReceived", execution.getEventName());
	}
}