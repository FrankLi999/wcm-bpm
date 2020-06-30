package com.bpwizard.wcm.repo.rest.jcr.model;

public class CancelDraftRequest {
	
	private String repository;
	private String wcmPath;
	private String contentId;
	private String reviewTaskId;
	private String editTaskId;
//	public static CancelDraftRequest createCancelDraftRequest(
//			String repository,
//			String wcmPath,
//			String contentId) {
//		
//		CancelDraftRequest cancelDraftRequest = new CancelDraftRequest();
//		cancelDraftRequest.setRepository(repository);
//		cancelDraftRequest.setContentId(contentId);
//		cancelDraftRequest.setWcmPath(wcmPath);
//		return cancelDraftRequest;
//	}
	
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
	@Override
	public String toString() {
		return "CancelDraftRequest [repository=" + repository + ", wcmPath=" + wcmPath + ", contentId=" + contentId
				+ ", reviewTaskId=" + reviewTaskId + ", editTaskId=" + editTaskId + "]";
	}	
}
