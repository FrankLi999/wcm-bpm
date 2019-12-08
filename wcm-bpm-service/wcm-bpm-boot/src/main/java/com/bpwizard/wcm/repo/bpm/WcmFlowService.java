package com.bpwizard.wcm.repo.bpm;

import java.util.HashMap;
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
	
	public String startContentFlow(
			String repository, 
			String workspace, 
			String contentPath,
			String contentId,
			String workflow) {
		Map<String, Object> variables = Variables.createVariables()
				.putValue("repository", repository)
				.putValue("workspace", workspace)
				.putValue("contentId", contentId)
		        .putValue("contentPath", contentPath);
		ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey(workflow, workflow + contentId, variables);
		//ProcessInstance processInstance = this.runtimeService.startProcessInstanceById(workflow, variables);
		return processInstance.getId();
	}
	
	public void deleteDraft(String workflow, String contentId) {
//		this.runtimeService.createMessageCorrelation("delete-draft-message")
//		    .processInstanceBusinessKey("wcm_content_flow")
//		    .processInstanceVariableEquals("contentId", contentId) 
//		    .correlate();
		Map<String,Object> correlation = new HashMap<>();
		correlation.put("contentId", contentId);
		this.runtimeService.correlateMessage("deleteDraftMessage", workflow + contentId, correlation);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>> delete draft :");
	}
}
