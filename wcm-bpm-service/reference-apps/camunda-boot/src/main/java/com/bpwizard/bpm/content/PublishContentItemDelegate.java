package com.bpwizard.bpm.content;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class PublishContentItemDelegate implements JavaDelegate {

	@Override
	public void execute(DelegateExecution delegate) throws Exception {
		
		String repository = (String) delegate.getVariable("repository");
		String workspace = (String) delegate.getVariable("workspace");
		String contentPath = (String) delegate.getVariable("contentPath");
		
	}

}
