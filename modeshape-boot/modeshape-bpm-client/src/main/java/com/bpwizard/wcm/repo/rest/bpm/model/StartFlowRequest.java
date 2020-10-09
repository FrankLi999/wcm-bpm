package com.bpwizard.wcm.repo.rest.bpm.model;

public class StartFlowRequest {
	String author;
	String repository;
	String workspace;
	String wcmPath;
	String contentId;
	String baseUrl;
	String workflow;
	
	public static StartFlowRequest createStartContentFlowRequest(
			String author,
			String repository,
			String workspace,
			String contentId,
			String wcmPath,
			String baseUrl,
			String workflow) {
		
		StartFlowRequest request = new StartFlowRequest();
		request.setAuthor(author);
		request.setRepository(repository);
		request.setWorkspace(workspace);
		request.setContentId(contentId);
		request.setWcmPath(wcmPath);
		request.setBaseUrl(baseUrl);
		request.setWorkflow(workflow);
		
		return request;
	}
	public String getAuthor() {
		return author;
	}

	private void setAuthor(String author) {
		this.author = author;
	}

	public String getRepository() {
		return repository;
	}
	private void setRepository(String repository) {
		this.repository = repository;
	}
	public String getWorkspace() {
		return workspace;
	}
	private void setWorkspace(String workspace) {
		this.workspace = workspace;
	}
	
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	private void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public String getWcmPath() {
		return wcmPath;
	}
	private void setWcmPath(String wcmPath) {
		this.wcmPath = wcmPath;
	}
	public String getWorkflow() {
		return workflow;
	}
	private void setWorkflow(String workflow) {
		this.workflow = workflow;
	}
	@Override
	public String toString() {
		return "StartFlowRequest [author=" + author + ", repository=" + repository + ", workspace=" + workspace
				+ ", wcmPath=" + wcmPath + ", contentId=" + contentId + ", baseUrl=" + baseUrl + ", workflow="
				+ workflow + "]";
	}
}
