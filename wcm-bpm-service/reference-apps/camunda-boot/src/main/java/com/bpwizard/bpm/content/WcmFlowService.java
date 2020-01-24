package com.bpwizard.bpm.content;

import java.util.Map;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.MessageCorrelationBuilder;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.SignalEventReceivedBuilder;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


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
	
	public String startContentFlowWithMessage(
			String repository, 
			String workspace, 
			String contentPath,
			String contentId,
			String workflow) {
		
		ProcessInstance processInstance = runtimeService
				  .createMessageCorrelation("startContentFlowMessage")
				  .processInstanceBusinessKey(workflow + contentId)
				  .setVariable("repository", repository)
				  .setVariable("workspace", workspace)
				  .setVariable("contentId", contentId)
				  .setVariable("contentPath", contentPath)
				  .correlateStartMessage();
//				  //.correlateAllWithResult();
		
//		Map<String, Object> variables = Variables.createVariables()
//				.putValue("repository", repository)
//				.putValue("workspace", workspace)
//				.putValue("contentId", contentId)
//		        .putValue("contentPath", contentPath);
//		ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey(workflow, workflow + contentId, variables);
		//ProcessInstance processInstance = this.runtimeService.startProcessInstanceById(workflow, variables);
		return processInstance.getId();
	}
	
	public void sendMessage(String message, String businessKey, Map<String, Object> variables) {
		MessageCorrelationBuilder builder = runtimeService.createMessageCorrelation(message);
		if (StringUtils.hasText(businessKey)) {
			builder.processInstanceBusinessKey(businessKey);
		}
		if (variables != null) {
			for (String key: variables.keySet()) {
				builder.setVariable(key, variables.get(key));
			}
		}
//		  .processInstanceBusinessKey(workflow + contentId)
//		  .setVariable("contentId", contentId)
		MessageCorrelationResult result = builder.correlateWithResult();
		System.out.println(">>>>>>>>>>>>>>>>>>>>>> message sent :" + result);
	}
	
//	public void deleteReviewingDraft(String workflow, String contentId) {
//		MessageCorrelationResult result = runtimeService
//				  .createMessageCorrelation("deleteReviewingDraftMessage")
//				  .processInstanceBusinessKey(workflow + contentId)
//				  .setVariable("contentId", contentId)
//				  .correlateWithResult();
//				  //.correlateAllWithResult();
//		
////		this.runtimeService.createMessageCorrelation("deleteReviewingDraftMessage")
////		    .processInstanceBusinessKey(workflow + contentId)
////		    .processInstanceVariableEquals("contentId", contentId) 
////		    .correlate();
////		Map<String,Object> correlation = new HashMap<>();
////		correlation.put("contentId", contentId);
//		
//		// this.runtimeService.correlateMessage("deleteReviewingDraftMessage", workflow + contentId, correlation);
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>> delete draft :" + result);
//	}
	
//	public void deleteEditingDraft(String workflow, String contentId) {
////		this.runtimeService.createMessageCorrelation("delete-draft-message")
////		    .processInstanceBusinessKey("wcm_content_flow")
////		    .processInstanceVariableEquals("contentId", contentId) 
////		    .correlate();
//		Map<String,Object> variables = new HashMap<>();
//		variables.put("contentId", contentId);
//		this.runtimeService
//		  .createSignalEvent(String.format("deleteEditingDraftSignal-%s", workflow + contentId))
//		  //.setVariables(variables)
//		  .send();
//		
//		//this.runtimeService.correlateMessage("deleteEditingDraftMessage", workflow + contentId, correlation);
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>> delete draft :");
//	}
	
	public void sendSignal(String signalName, Map<String,Object> variables) {
//		this.runtimeService.createMessageCorrelation("delete-draft-message")
//		    .processInstanceBusinessKey("wcm_content_flow")
//		    .processInstanceVariableEquals("contentId", contentId) 
//		    .correlate();
		//Map<String,Object> variables = new HashMap<>();
		// variables.put("contentId", contentId);
		SignalEventReceivedBuilder builder = this.runtimeService
		  // .createSignalEvent(String.format("deleteEditingDraftSignal-%s", workflow + contentId))
		  .createSignalEvent(signalName);
		if (variables != null) {
			builder = builder.setVariables(variables);
		}
		builder.send();
		
		//this.runtimeService.correlateMessage("deleteEditingDraftMessage", workflow + contentId, correlation);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>> delete draft :");
	}
}
