package com.bpwizard.bpm.content.model;

public class StartFlowRequest {
	String repository;
	String workspace;
	String contentPath;
	String contentId;
	String workflow;
	
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
	public String getWorkspace() {
		return workspace;
	}
	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}
	public String getContentPath() {
		return contentPath;
	}
	public void setContentPath(String contentPath) {
		this.contentPath = contentPath;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getWorkflow() {
		return workflow;
	}
	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}
	@Override
	public String toString() {
		return "StartFlowRequest [repository=" + repository + ", workspace=" + workspace + ", contentPath="
				+ contentPath + ", contentId=" + contentId + ", workflow=" + workflow + "]";
	}
}
