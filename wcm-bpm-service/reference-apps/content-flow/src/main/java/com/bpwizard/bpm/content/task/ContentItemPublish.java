package com.bpwizard.bpm.content.task;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ContentItemPublish implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String var = (String) execution.getVariable("input");
		var = var.toUpperCase();
		execution.setVariable("input", var);
	}
}
