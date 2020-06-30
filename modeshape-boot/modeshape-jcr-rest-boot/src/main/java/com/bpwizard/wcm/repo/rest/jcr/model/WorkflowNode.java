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
	private String processInstanceId;
	private String reviewTaskId;
	private String editTaskId;
	
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
	
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getReviewTaskId() {
		return reviewTaskId;
	}
	public void setReviewTaskId(String reviewTaskId) {
		this.reviewTaskId = reviewTaskId;
	}
	public String getEditTaskId() {
		return editTaskId;
	}
	public void setEditTaskId(String editTaskId) {
		this.editTaskId = editTaskId;
	}
	protected void toJson(ObjectNode jsonNode) {
		if (StringUtils.hasText(workflow)) {
			jsonNode.put("workflow", workflow);
		}

		if (StringUtils.hasText(workflowStage)) {
			jsonNode.put("workflowStage", workflowStage);
		}
		
		if (StringUtils.hasText(publishDate)) {
			jsonNode.put("publishDate", publishDate);
		}
		
		if (StringUtils.hasText(expireDate)) {
			jsonNode.put("expireDate", expireDate);
		}
		
		if (StringUtils.hasText(this.processInstanceId)) {
			jsonNode.put("processInstanceId", this.processInstanceId);
		}
		
		if (StringUtils.hasText(this.reviewTaskId)) {
			jsonNode.put("reviewTaskId", this.reviewTaskId);
		}
		
		if (StringUtils.hasText(this.editTaskId)) {
			jsonNode.put("editTaskId", this.editTaskId);
		}
	}
	@Override
	public String toString() {
		return "WorkflowNode [workflow=" + workflow + ", workflowStage=" + workflowStage + ", publishDate="
				+ publishDate + ", expireDate=" + expireDate + ", processInstanceId=" + processInstanceId 
				+ ", editTaskId=" + editTaskId + ", reviewTaskId=" + reviewTaskId + "]";
	}
}
