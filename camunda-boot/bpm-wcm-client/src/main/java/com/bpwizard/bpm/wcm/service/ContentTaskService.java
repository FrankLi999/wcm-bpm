package com.bpwizard.bpm.wcm.service;

import java.util.List;

import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.externaltask.ExternalTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bpwizard.bpm.wcm.client.model.ContentTask;

@Component
public class ContentTaskService {
	@Autowired
	ProcessEngine processEngine;
	
	@Autowired
	ContentTaskRepo reviewTasks;
	
	public ContentTask[] getActiveContentTasks(String topic) {
		return this.reviewTasks.getActiveTasksByTopic(topic);
	}
	
	public ContentTask[] getContentTasks(String topic) {
		ExternalTaskService externalTaskService = processEngine.getExternalTaskService();
	    List<ExternalTask> externalTasks = externalTaskService.createExternalTaskQuery()
		    	.topicName(topic)
		    	.active()
		    	.notLocked()
		    	.listPage(0, 10);
	    return externalTasks.stream().map(externalTask -> reviewTasks.findContentTask(externalTask.getId())).toArray(ContentTask[]::new);
	}
}
