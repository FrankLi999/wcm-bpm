package com.bpwizard.wcm.repo.rest.jcr.model;

public class DraftItemRequest {
	private String repository;
	private String wcmPath;
	private String contentId;
	private String processInstanceId;
	private String reviewTaskId;
	private String comment;
	private boolean approved;
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	@Override
	public String toString() {
		return "DraftItemRequest [repository=" + repository + ", wcmPath=" + wcmPath + ", contentId=" + contentId
				+ ", processInstanceId=" + processInstanceId + ", reviewTaskId=" + reviewTaskId + ", comment=" + comment
				+ ", approved=" + approved + "]";
	}
}
