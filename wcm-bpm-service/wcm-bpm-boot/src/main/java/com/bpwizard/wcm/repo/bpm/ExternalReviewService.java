package com.bpwizard.wcm.repo.bpm;

import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.externaltask.ExternalTask;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bpwizard.wcm.repo.rest.jcr.model.WcmRepository;

@Component
public class ExternalReviewService {
	@Autowired
	ProcessEngine processEngine;
//	private static final String EXTERNAL_WORKER_ID = "externalWorkerDelegate";
//	private static final String TOPIC_WCM_REVIEW = "wcm_review";
	LockedExternalTask lockedTask = null;
	
	public String[] getReviewTasks(String topic) {
		ExternalTaskService externalTaskService = processEngine.getExternalTaskService();
	    List<ExternalTask> externalTasks = externalTaskService.createExternalTaskQuery()
		    	.topicName(topic)
		    	.list();
	    return externalTasks.stream().map(ExternalTask::getId).toArray(String[]::new);
	}
	
	public String claimTask(String topic, String workerId) {
	    ExternalTaskService externalTaskService = processEngine.getExternalTaskService();
	    List<ExternalTask> externalTasks = externalTaskService.createExternalTaskQuery()
		    	.topicName(topic)
		    	.list();
	    
	    LockedExternalTask externalTask = externalTaskService.fetchAndLock(1, workerId).topic(topic, 24 * 60 * 1000).execute().get(0);
	    
	    
	    if (externalTask != null) {
	    	// ((ExternalTaskEntity)externalTask).lock(workerId, 24 * 60 * 1000);
	    	// externalTaskService.fetchAndLock(1, EXTERNAL_WORKER_ID).topic(TOPIC_WCM_REVIEW, 300000).execute();
	    	return externalTask.getId();
	    } else {
	    	return "n/a";
	    }
	}
	
	public String completeReview(String taskId, String topic, String workerId, boolean approved, String comment) {
	    ExternalTaskService externalTaskService = processEngine.getExternalTaskService();
	    ExternalTask externalTask = externalTaskService.createExternalTaskQuery()
	    		.externalTaskId(taskId)
	    		.locked()
	    		.workerId(workerId)
		    	.topicName(topic)
		    	.singleResult();
	    System.out.println(">>>>>>>>>>>>>>>>>> to complete review task: " + externalTask);
	    Map<String, Object> variables = Variables.createVariables().putValue("approved", approved).putValue("comment", comment);
	    externalTaskService.complete(externalTask.getId(), workerId, variables);
	    return "completed";
	}
}
