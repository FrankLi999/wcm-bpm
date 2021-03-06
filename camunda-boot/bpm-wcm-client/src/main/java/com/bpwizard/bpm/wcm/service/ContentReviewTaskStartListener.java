package com.bpwizard.bpm.wcm.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import com.bpwizard.bpm.wcm.client.model.WcmConstants;

public class ContentReviewTaskStartListener implements JavaDelegate {

	@Autowired
	ContentTaskRepo reviewTasks;
	private static final String TOPIC = "wcm_review";
	@Override
	public void execute(DelegateExecution delegate) throws Exception {
		String activityId = delegate.getCurrentActivityId();
		System.out.println(">>>>>>>>>>>>>>>>>start activiy id:" + delegate.getCurrentActivityId());
		System.out.println(">>>>>>>>>>>>>>>>>start activiy instance id:" + delegate.getActivityInstanceId());
		System.out.println(">>>>>>>>>>>>>>>>>start id:" + delegate.getId());
		String repository = (String) delegate.getVariable("repository");
		String contentPath = (String) delegate.getVariable("wcmPath");
		String contentId = (String) delegate.getVariable("contentId");
		this.reviewTasks.registerContentTask(
				activityId,
				TOPIC,
				repository,
				WcmConstants.DRAFT_WS,
				contentPath,
				contentId);
	}
}
