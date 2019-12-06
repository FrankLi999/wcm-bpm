package com.bpwizard.wcm.repo.bpm;

import java.util.Map;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WcmFlowService {
	@Autowired
	RuntimeService runtimeService;
	
	public String startContentFlow(String repository, String workspace, String contentPath, String workflow) {
		Map<String, Object> variables = Variables.createVariables()
				.putValue("repository", repository)
				.putValue("workspace", workspace)
		        .putValue("contentPath", contentPath);
		ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey(workflow, variables);
		//ProcessInstance processInstance = this.runtimeService.startProcessInstanceById(workflow, variables);
		return processInstance.getId();
	}
}
