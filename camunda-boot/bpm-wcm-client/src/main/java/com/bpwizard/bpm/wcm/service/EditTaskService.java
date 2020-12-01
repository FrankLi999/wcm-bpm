package com.bpwizard.bpm.wcm.service;

import java.util.List;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EditTaskService {
	@Autowired
	ProcessEngine processEngine;

	public String claimTask(String contentId, String taskName, String userId) {
//		ExternalTaskService externalTaskService = processEngine.getExternalTaskService();
//	    LockedExternalTask externalTask = externalTaskService.fetchAndLock(1, workerId).topic(topic, 24 * 60 * 1000).processInstanceVariableEquals("contentId", contentId).execute().get(0);
//	    
//	    if (externalTask != null) {
//	    	// ((ExternalTaskEntity)externalTask).lock(workerId, 24 * 60 * 1000);
//	    	// externalTaskService.fetchAndLock(1, EXTERNAL_WORKER_ID).topic(TOPIC_WCM_EDIT, 300000).execute();
//	    	return externalTask.getId();
//	    } else {
//	    	return "n/a";
//	    }
		
		List<Task> tasks = this.processEngine.getTaskService().createTaskQuery()
	            //.processInstanceId(workflow)
	    		//.processInstanceBusinessKey(ContentServerUtils.getBusinessKey(workflow, contentId))
				.processVariableValueEquals("contentId", contentId)
				.taskUnassigned()
				.taskDefinitionKey(taskName)
				//.taskName("review-content")
				.list();
		
	    String userTaskId = null;
	    if (tasks != null && tasks.size() > 0) {
		    userTaskId = tasks.get(0).getId();
		    this.processEngine.getTaskService().claim(userTaskId, userId);
		    // return String.format("%s~~%s", externalTaskId, userTaskId);
	    }
	    return userTaskId;
	}
	
	public String getEditTaskId(String contentId, String taskName) {
		List<Task> tasks = this.processEngine.getTaskService().createTaskQuery()
				.processVariableValueEquals("contentId", contentId)
				.taskUnassigned()
				.taskDefinitionKey(taskName)
				.list();
		
	    String userTaskId = null;
	    if (tasks != null && tasks.size() > 0) {
		    userTaskId = tasks.get(0).getId();
	    }
	    return userTaskId;
	}
	
	//One transaction
	public String completeEdit(String taskId) {
//	    ExternalTaskService externalTaskService = processEngine.getExternalTaskService();
//	    ExternalTask externalTask = externalTaskService.createExternalTaskQuery()
//	    		.externalTaskId(taskId)
//	    		.locked()
//	    		.workerId(workerId)
//		    	.topicName(topic)
//		    	.singleResult();
//	 
//	    if (externalTask != null) {
//	    	externalTaskService.complete(externalTask.getId(), workerId);
//	    	return "completed";
//	    } else {
//	    	return "n/a";
//	    }
		
		TaskQuery userTaskQuery = this.processEngine.getTaskService().createTaskQuery();
	    Task task = userTaskQuery.taskId(taskId).singleResult();
	    String userTaskId = task.getId();
	    this.processEngine.getTaskService().complete(userTaskId);
	    
//	    if (externalTask != null) {
//	    	externalTaskService.complete(externalTask.getId(), workerId, variables);
//	    	return "completed";
//	    } else {
//	    	return "n/a";
//	    }
	    return "completed " + userTaskId;
	}
}
