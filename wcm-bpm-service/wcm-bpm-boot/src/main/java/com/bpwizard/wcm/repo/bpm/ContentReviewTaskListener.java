package com.bpwizard.wcm.repo.bpm;

import java.util.List;

import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.externaltask.ExternalTask;

public class ContentReviewTaskListener implements JavaDelegate {

	@Override
	public void execute(DelegateExecution delegate) throws Exception {
		System.out.println(">>>>>>>>>>>>>> ActivityInstanceId " + delegate.getActivityInstanceId());
		System.out.println(">>>>>>>>>>>>>> BusinessKey " + delegate.getBusinessKey());
		System.out.println(">>>>>>>>>>>>>> Id " + delegate.getId());
		System.out.println(">>>>>>>>>>>>>> CurrentActivityId " + delegate.getCurrentActivityId());
		ExternalTaskService externalTaskService = delegate.getProcessEngine().getExternalTaskService();
		ExternalTask externalTask = externalTaskService.createExternalTaskQuery()
		    	.processDefinitionId(delegate.getProcessInstanceId())
		    	.externalTaskId(delegate.getId())
		    	//.activityId(activityId)
		    	//.topicName(topic)
		    	.singleResult();
		    
		    List<ExternalTask> externalTasks = externalTaskService.createExternalTaskQuery()
			    	.processDefinitionId(delegate.getProcessInstanceId())
			    	.activityId(delegate.getCurrentActivityId()).list();
		  
		    List<ExternalTask> externalTask0 = externalTaskService.createExternalTaskQuery()
			    	.processDefinitionId(delegate.getProcessInstanceId())
			    	.externalTaskId(delegate.getId())
			    	//.activityId(activityId)
			    	//.topicName(topic)
			    	.list();
		    List<ExternalTask> externalTask1 = externalTaskService.createExternalTaskQuery()
			    	.processDefinitionId(delegate.getProcessInstanceId())
			    	.externalTaskId(delegate.getActivityInstanceId())
			    	//.activityId(activityId)
			    	//.topicName(topic)
			    	.list();
		    List<ExternalTask> externalTasks2 = externalTaskService.createExternalTaskQuery()
			    	.processDefinitionId(delegate.getProcessInstanceId())
			    	.list();
		   
		    List<ExternalTask> externalTasks3 = externalTaskService.createExternalTaskQuery()
			    	.processDefinitionId(delegate.getProcessInstanceId())
			    	.activityId(delegate.getId())
			    	.list();
		    List<ExternalTask> externalTasks4 = externalTaskService.createExternalTaskQuery()
			    	.processDefinitionId(delegate.getProcessInstanceId())
			    	.activityId(delegate.getActivityInstanceId())
			    	.list();
		    System.out.println(">>>>>>>>>>> getActivityInstanceId():" + delegate.getActivityInstanceId());
	}

	

}
