package com.bpwizard.wcm.repo.bpm;

import java.util.List;

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
//	private static final String EXTERNAL_WORKER_ID = "externalWorkerDelegate";
//	private static final String TOPIC_WCM_REVIEW = "wcm_review";
	LockedExternalTask lockedTask = null;
	public String[] getEditTasks(String topic) {
		ExternalTaskService externalTaskService = processEngine.getExternalTaskService();
	    List<ExternalTask> externalTasks = externalTaskService.createExternalTaskQuery()
		    	.topicName(topic)
		    	.list();
	    return externalTasks.stream().map(ExternalTask::getId).toArray(String[]::new);
	}
	
	
	public String claimTask(String topic, String workerId) {
		ExternalTaskService externalTaskService = processEngine.getExternalTaskService();
	    LockedExternalTask externalTask = externalTaskService.fetchAndLock(1, workerId).topic(topic, 24 * 60 * 1000).execute().get(0);
	    
	    
	    if (externalTask != null) {
	    	// ((ExternalTaskEntity)externalTask).lock(workerId, 24 * 60 * 1000);
	    	// externalTaskService.fetchAndLock(1, EXTERNAL_WORKER_ID).topic(TOPIC_WCM_EDIT, 300000).execute();
	    	return externalTask.getId();
	    } else {
	    	return "n/a";
	    }
	}
	
	public String completeEdit(String taskId, String topic, String workerId) {
	    ExternalTaskService externalTaskService = processEngine.getExternalTaskService();
	    ExternalTask externalTask = externalTaskService.createExternalTaskQuery()
	    		.externalTaskId(taskId)
	    		.locked()
	    		.workerId(workerId)
		    	.topicName(topic)
		    	.singleResult();
	    externalTaskService.complete(externalTask.getId(), workerId);
	    return "completed";
	}
}
