package com.bpwizard.bpm.content;

import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.externaltask.ExternalTask;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExternalEditService {
	@Autowired
	ProcessEngine processEngine;

	@Autowired
	ReviewTaskRepo reviewTasks;
	
//	public ReviewTask[] getEditTasks(String topic) {
//		ExternalTaskService externalTaskService = processEngine.getExternalTaskService();
//	    List<ExternalTask> externalTasks = externalTaskService.createExternalTaskQuery()
//		    	.topicName(topic)
//		    	.active()
//		    	.notLocked()
//		    	.listPage(0, 10);
//	    return externalTasks.stream().map(externalTask -> reviewTasks.findReviewTask(externalTask.getId())).toArray(ReviewTask[]::new);
//	}
	
	
	public String claimTask(String contentId, String topic, String workerId) {
		ExternalTaskService externalTaskService = processEngine.getExternalTaskService();
	    LockedExternalTask externalTask = externalTaskService.fetchAndLock(1, workerId).topic(topic, 24 * 60 * 1000).processInstanceVariableEquals("contentId", contentId).execute().get(0);
	    
	    if (externalTask != null) {
	    	// ((ExternalTaskEntity)externalTask).lock(workerId, 24 * 60 * 1000);
	    	// externalTaskService.fetchAndLock(1, EXTERNAL_WORKER_ID).topic(TOPIC_WCM_EDIT, 300000).execute();
	    	return externalTask.getId();
	    } else {
	    	return "n/a";
	    }
	}
	
	//One transaction
	public String completeEdit(
			String taskId, 
			String topic, 
			String workerId) {
	    ExternalTaskService externalTaskService = processEngine.getExternalTaskService();
	    ExternalTask externalTask = externalTaskService.createExternalTaskQuery()
	    		.externalTaskId(taskId)
	    		.locked()
	    		.workerId(workerId)
		    	.topicName(topic)
		    	.singleResult();
	 
	    if (externalTask != null) {
	    	externalTaskService.complete(externalTask.getId(), workerId);
	    	return "completed";
	    } else {
	    	return "n/a";
	    }
	}
}
