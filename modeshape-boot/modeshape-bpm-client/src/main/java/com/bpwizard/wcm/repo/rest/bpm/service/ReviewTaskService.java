package com.bpwizard.wcm.repo.rest.bpm.service;

import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewTaskService {
	@Autowired
	ProcessEngine processEngine;
	
	public String claimTask(String contentId, String taskName, String userId) {
		List<Task> tasks = this.processEngine.getTaskService().createTaskQuery()
				.processVariableValueEquals("contentId", contentId)
				.taskUnassigned()
				.taskDefinitionKey(taskName)
				.list();
		
	    String userTaskId = null;
	    if (tasks != null && tasks.size() > 0) {
		    userTaskId = tasks.get(0).getId();
		    this.processEngine.getTaskService().claim(userTaskId, userId);
	    }
	    return userTaskId;
	}
	
	public String getReviewTaskId(String contentId, String taskName) {
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
	
	public String completeReview(
			String taskId,  
			boolean approved, 
			String comment,
			String token,
			String publishServiceUrl) {
	    
	    TaskQuery userTaskQuery = this.processEngine.getTaskService().createTaskQuery();
	    Task task = userTaskQuery.taskId(taskId).singleResult();
	    String userTaskId = task.getId();
	    Map<String, Object> userTaskVariables = Variables.createVariables()
	    		.putValue("publishServiceUrl", publishServiceUrl)
	    		.putValue("token", token)
	    		.putValue("comment", comment)
	    		.putValue("reviewRejected", approved ? Boolean.FALSE: Boolean.TRUE);
	    this.processEngine.getTaskService().complete(userTaskId, userTaskVariables);
	    return "completed " + userTaskId;
	}
	
	
}
