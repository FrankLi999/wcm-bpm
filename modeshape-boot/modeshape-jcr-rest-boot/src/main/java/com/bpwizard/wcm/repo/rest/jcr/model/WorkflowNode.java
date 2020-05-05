package com.bpwizard.wcm.repo.rest.jcr.model;

import java.io.Serializable;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class WorkflowNode  implements Serializable {
	private static final long serialVersionUID = -2123676751280039604L;
	private String workflow;
	private String workflowStage;
	private String publishDate;
	private String expireDate;
	private String currentLifecycleState;
	
	public String getWorkflow() {
		return workflow;
	}
	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}
	public String getWorkflowStage() {
		return workflowStage;
	}
	public void setWorkflowStage(String workflowStage) {
		this.workflowStage = workflowStage;
	}
	
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}
	
	
	public String getCurrentLifecycleState() {
		return currentLifecycleState;
	}
	public void setCurrentLifecycleState(String currentLifecycleState) {
		this.currentLifecycleState = currentLifecycleState;
	}
	protected void toJson(ObjectNode jsonNode) {
		if (StringUtils.hasText(workflow)) {
			jsonNode.put("bpw:workflow", workflow);
		}

		if (StringUtils.hasText(workflowStage)) {
			jsonNode.put("bpw:workflowStage", workflowStage);
		}
		
		if (StringUtils.hasText(publishDate)) {
			jsonNode.put("bpw:publishDate", publishDate);
		}
		
		if (StringUtils.hasText(expireDate)) {
			jsonNode.put("bpw:expireDate", expireDate);
		}
		
		if (StringUtils.hasText(currentLifecycleState)) {
			jsonNode.put("bpw:currentLifecycleState", currentLifecycleState);
		}
	}
	@Override
	public String toString() {
		return "WorkflowNode [workflow=" + workflow + ", workflowStage=" + workflowStage + ", publishDate="
				+ publishDate + ", expireDate=" + expireDate + ", currentLifecycleState=" + currentLifecycleState
				+ ", toString()=" + super.toString() + "]";
	}
}
