package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class ResourceNode {
	private String title;
	private String description;
	private String[] workflow;
	private String workflowStage;
	private String[] categories;
	private String publishDate;
	private String expireDate;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String[] getWorkflow() {
		return workflow;
	}
	public void setWorkflow(String[] workflow) {
		this.workflow = workflow;
	}
	public String getWorkflowStage() {
		return workflowStage;
	}
	public void setWorkflowStage(String workflowStage) {
		this.workflowStage = workflowStage;
	}
	public String[] getCategories() {
		return categories;
	}
	public void setCategories(String[] categories) {
		this.categories = categories;
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
	@Override
	public String toString() {
		return "ResourceNode [title=" + title + ", description=" + description + ", workflow="
				+ Arrays.toString(workflow) + ", workflowStage=" + workflowStage + ", categories="
				+ Arrays.toString(categories) + ", publishDate=" + publishDate + ", expireDate=" + expireDate + "]";
	}
}
