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
	private String reviewer;
	private String editor;
	
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
	public String getReviewer() {
		return reviewer;
	}
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
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
		
		if (StringUtils.hasText(this.reviewer)) {
			jsonNode.put("reviewer", this.reviewer);
		}
		
		if (StringUtils.hasText(this.editor)) {
			jsonNode.put("editor", this.editor);
		}
	}
	@Override
	public String toString() {
		return "WorkflowNode [workflow=" + workflow + ", workflowStage=" + workflowStage + ", publishDate="
				+ publishDate + ", expireDate=" + expireDate + ", processInstanceId=" + processInstanceId
				+ ", reviewer=" + reviewer + ", editor=" + editor + "]";
	}
}
