package com.bpwizard.bpm.content;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

public class ContentReviewTaskStartListener implements JavaDelegate {

	@Autowired
	ReviewTaskRepo reviewTasks;
	private static final String TOPIC = "wcm_review";
	@Override
	public void execute(DelegateExecution delegate) throws Exception {
		String activityId = delegate.getCurrentActivityId();
		System.out.println(">>>>>>>>>>>>>>>>>start activiy id:" + delegate.getCurrentActivityId());
		System.out.println(">>>>>>>>>>>>>>>>>start activiy instance id:" + delegate.getActivityInstanceId());
		System.out.println(">>>>>>>>>>>>>>>>>start id:" + delegate.getId());
		String repository = (String) delegate.getVariable("repository");
		String workspace = (String) delegate.getVariable("workspace");
		String contentPath = (String) delegate.getVariable("contentPath");
		String contentId = (String) delegate.getVariable("contentId");
		this.reviewTasks.registerReviewTask(
				activityId,
				TOPIC,
				repository,
				workspace,
				contentPath,
				contentId);
	}
}
