package com.bpwizard.bpm.content;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class PublishContentItemDelegate implements JavaDelegate {

	@Autowired
	RestTemplate restTemplate;
	
	@Override
	public void execute(DelegateExecution delegate) throws Exception {
		
		String repository = (String) delegate.getVariable("repository");
		String workspace = (String) delegate.getVariable("workspace");
		String wcmPath = (String) delegate.getVariable("wcmPath");
		String contentId = (String) delegate.getVariable("contentId");
	
	}

}
