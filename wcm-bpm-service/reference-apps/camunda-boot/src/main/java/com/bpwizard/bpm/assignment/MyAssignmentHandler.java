package com.bpwizard.bpm.assignment;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

public class MyAssignmentHandler implements TaskListener {
	public void notify(DelegateTask delegateTask) {
		
		// Execute custom identity lookups here
		// and then for example call following methods:
		// delegateTask.setAssignee("kermit");
		// delegateTask.addCandidateUser("fozzie");
		System.out.println(">>>>>>>>>>>. Is authorization enabled :" +
		    delegateTask.getProcessEngine().getProcessEngineConfiguration().isAuthorizationEnabled());
		delegateTask.addCandidateGroup("management");
	}
}