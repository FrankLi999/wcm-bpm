package com.bpwizard.wcm.repo.rest.jcr.model;

public class CancelDraftRequest {
	
	private String repository;
	private String wcmPath;
	private String contentId;
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
	public String getWcmPath() {
		return wcmPath;
	}
	public void setWcmPath(String wcmPath) {
		this.wcmPath = wcmPath;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	@Override
	public String toString() {
		return "CancelDraftRequest [repository=" + repository + ", wcmPath=" + wcmPath + ", contentId=" + contentId
				+ "]";
	}

	
}
