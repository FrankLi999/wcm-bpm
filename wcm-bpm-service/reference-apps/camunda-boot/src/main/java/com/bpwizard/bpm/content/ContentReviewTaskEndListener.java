package com.bpwizard.bpm.content;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

public class ContentReviewTaskEndListener implements JavaDelegate {

	@Autowired
	ReviewTaskRepo reviewTasks;
	@Override
	public void execute(DelegateExecution delegate) throws Exception {
		String activityId = delegate.getCurrentActivityId();
		System.out.println(">>>>>>>>>>>>>>>>>>>>> activity comlete:" + activityId);
		System.out.println(">>>>>>>>>>>>>>>>>>>>> activity instance id comlete1:" + delegate.getActivityInstanceId());
		System.out.println(">>>>>>>>>>>>>>>>>>>>> activity id comlete1:" + delegate.getId());
		// this.reviewTasks.removeReviewTask(activityId);
	}
}
