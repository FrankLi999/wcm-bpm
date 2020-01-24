package com.bpwizard.bpm.content;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class SaveDraftDelegate implements JavaDelegate {

	@Override
	public void execute(DelegateExecution delegate) throws Exception {

		String repository = (String) delegate.getVariable("repository");
		String workspace = (String) delegate.getVariable("workspace");
		String contentPath = (String) delegate.getVariable("contentPath");
		System.out.println(">>>>>>>>>>>>>>>>>> repository:" + repository);
		System.out.println(">>>>>>>>>>>>>>>>>> workspace:" + workspace);
		System.out.println(">>>>>>>>>>>>>>>>>> contentPath:" + contentPath);
		
		String processInstanceId = delegate.getProcessInstance().getId();
		System.out.println(">>>>>>>>>>>>>>>>>> processInstanceId:" + processInstanceId);

		String contentReviewActivityId = "review-content-Item";
		System.out.println(">>>>>>>>>>>>>>>>>> contentReviewActivityId:" + contentReviewActivityId);
	}
}
