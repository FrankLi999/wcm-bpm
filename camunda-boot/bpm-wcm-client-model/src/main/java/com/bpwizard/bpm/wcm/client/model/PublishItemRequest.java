package com.bpwizard.bpm.wcm.client.model;

public class PublishItemRequest {
	private String repository;
	private String wcmPath;
	private String contentId;
	
	public static PublishItemRequest createPublishItemRequest(
			String repository,
			String wcmPath,
			String contentId) {
		PublishItemRequest publishItemRequest = new PublishItemRequest();
		publishItemRequest.setContentId(contentId);
		publishItemRequest.setRepository(repository);
		publishItemRequest.setWcmPath(wcmPath);
		return publishItemRequest;
	}
	public String getRepository() {
		return repository;
	}
	private void setRepository(String repository) {
		this.repository = repository;
	}
	public String getWcmPath() {
		return wcmPath;
	}
	private void setWcmPath(String wcmPath) {
		this.wcmPath = wcmPath;
	}
	public String getContentId() {
		return contentId;
	}
	private void setContentId(String contentId) {
		this.contentId = contentId;
	}
	@Override
	public String toString() {
		return "PublishItemRequest [repository=" + repository + ", wcmPath=" + wcmPath + ", contentId=" + contentId
				+ "]";
	}
}
