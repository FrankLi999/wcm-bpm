package com.bpwizard.wcm.repo.rest.jcr.model;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class WorkflowNode extends ResourceNode {
	private String workflow;
	private String workflowStage;
	private String publishDate;
	private String expireDate;
	
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
	
	protected void toJson(ObjectNode properties, ObjectNode children) {
		super.toJson(properties, children);

		if (StringUtils.hasText(workflow)) {
			properties.put("bpw:workflow", workflow);
		}

		if (StringUtils.hasText(workflowStage)) {
			properties.put("bpw:workflowStage", workflowStage);
		}
		
		if (StringUtils.hasText(publishDate)) {
			properties.put("bpw:publishDate", publishDate);
		}
		
		if (StringUtils.hasText(expireDate)) {
			properties.put("bpw:expireDate", expireDate);
		}
	}
	
	@Override
	public String toString() {
		return "WorkflowNode [workflow=" + workflow + ", workflowStage=" + workflowStage + ", publishDate=" + publishDate + ", expireDate=" + expireDate
				+ ", toString()=" + super.toString() + "]";
	}
}
