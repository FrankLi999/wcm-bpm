package com.bpwizard.bpm.content;

import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.externaltask.ExternalTask;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bpwizard.bpm.content.model.ReviewTask;

@Component
public class ExternalReviewService {
	@Autowired
	ProcessEngine processEngine;
	
	@Autowired
	ReviewTaskRepo reviewTasks;
	
	public ReviewTask[] getReviewTasks(String topic) {
		return this.reviewTasks.getReviewTasksByTopic(topic);
	}
	
	public String claimTask(String contentId, String topic, String workerId) {
		
	    ExternalTaskService externalTaskService = processEngine.getExternalTaskService();
	    List<LockedExternalTask> externalTasks = externalTaskService.fetchAndLock(1, workerId, true).topic(topic, 24 * 60 * 1000).processInstanceVariableEquals("contentId", contentId).execute();
	    LockedExternalTask externalTask = (externalTasks != null && externalTasks.size() > 0) ? externalTasks.get(0) : null;
	    if (externalTask != null) {
	    	return externalTask.getId();
	    } else {
	    	return "n/a";
	    }
	}
	
	public String completeReview(
			String taskId, 
			String topic, 
			String workerId, 
			boolean approved, 
			String comment) {
		
	    ExternalTaskService externalTaskService = processEngine.getExternalTaskService();
	    ExternalTask externalTask = externalTaskService.createExternalTaskQuery()
	    		.externalTaskId(taskId)
	    		.locked()
	    		.workerId(workerId)
		    	.topicName(topic)
		    	.singleResult();
	    System.out.println(">>>>>>>>>>>>>>>>>> to complete review task: " + externalTask);
	    //save content, with comment
	    Map<String, Object> variables = approved ? Variables.createVariables().putValue("reviewRejected", Boolean.FALSE) : Variables.createVariables().putValue("reviewRejected", Boolean.TRUE).putValue("comment", comment);
	    if (externalTask != null) {
	    	externalTaskService.complete(externalTask.getId(), workerId, variables);
	    	return "completed";
	    } else {
	    	return "n/a";
	    }
	    
	}
}
