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
		
//	    ExternalTaskService externalTaskService = processEngine.getExternalTaskService();
//	    List<LockedExternalTask> externalTasks = externalTaskService.fetchAndLock(1, workerId, true).topic(topic, 24 * 60 * 1000).processInstanceVariableEquals("contentId", contentId).execute();
//	    LockedExternalTask externalTask = (externalTasks != null && externalTasks.size() > 0) ? externalTasks.get(0) : null;
//	    String externalTaskId = (externalTask != null) ? externalTask.getId() : "n/a";
		List<Task> tasks = this.processEngine.getTaskService().createTaskQuery()
	            //.processInstanceId(workflow)
	    		//.processInstanceBusinessKey(ContentServerUtils.getBusinessKey(workflow, contentId))
				.processVariableValueEquals("contentId", contentId)
				.taskUnassigned()
				.taskDefinitionKey(taskName)
				//.taskName("review-content")
				.list();
		
	    String userTaskId = "n/a";
	    if (tasks != null && tasks.size() > 0) {
		    userTaskId = tasks.get(0).getId();
		    this.processEngine.getTaskService().claim(userTaskId, userId);
		    // return String.format("%s~~%s", externalTaskId, userTaskId);
	    }
	    return userTaskId;
	}
	
	public String completeReview(
			String taskId,  
			boolean approved, 
			String comment) {
		
//	    ExternalTaskService externalTaskService = processEngine.getExternalTaskService();
//	    ExternalTask externalTask = externalTaskService.createExternalTaskQuery()
//	    		.externalTaskId(taskId)
//	    		.locked()
//	    		.workerId(workerId)
//		    	.topicName(topic)
//		    	.singleResult();
//	    System.out.println(">>>>>>>>>>>>>>>>>> to complete review task: " + externalTask);
	    
	    //save content, with comment
	    
	    		
	    // Map<String, Object> variables = approved ? Variables.createVariables().putValue("reviewRejected", Boolean.FALSE) : Variables.createVariables().putValue("reviewRejected", Boolean.TRUE).putValue("comment", comment);
	    
	    TaskQuery userTaskQuery = this.processEngine.getTaskService().createTaskQuery();
	    Task task = userTaskQuery.taskId(taskId).singleResult();
	    String userTaskId = task.getId();
	    Map<String, Object> userTaskVariables = approved ? Variables.createVariables().putValue("reviewRejected", Boolean.FALSE) : Variables.createVariables().putValue("reviewRejected", Boolean.TRUE).putValue("comment", comment);
	    this.processEngine.getTaskService().complete(userTaskId, userTaskVariables);
	    
//	    if (externalTask != null) {
//	    	externalTaskService.complete(externalTask.getId(), workerId, variables);
//	    	return "completed";
//	    } else {
//	    	return "n/a";
//	    }
	    return "completed " + userTaskId;
	}
	
	
}
