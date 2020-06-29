package com.bpwizard.wcm.repo.rest.bpm.model;

public class CompleteReviewRequest {
	private String reviewTaskId;
	private boolean approved;
	private String comment;
	private String token;
	private String publishServiceUrl;
	public static CompleteReviewRequest createCompleteReviewRequest(
			String reviewTaskId, 
			boolean approved, 
			String comment, 
			String token,
			String publishServiceUrl) {
		CompleteReviewRequest completeReviewRequest = new CompleteReviewRequest();
		completeReviewRequest.setApproved(approved);
		completeReviewRequest.setReviewTaskId(reviewTaskId);
		completeReviewRequest.setComment(comment);
		completeReviewRequest.setToken(token);
		completeReviewRequest.setPublishServiceUrl(publishServiceUrl);
		return completeReviewRequest;
	}
	public String getReviewTaskId() {
		return reviewTaskId;
	}
	
	private void setReviewTaskId(String taskId) {
		this.reviewTaskId = taskId;
	}
	
	public boolean isApproved() {
		return approved;
	}
	
	private void setApproved(boolean approved) {
		this.approved = approved;
	}
	
	public String getComment() {
		return comment;
	}
	
	private void setComment(String comment) {
		this.comment = comment;
	}

	public String getToken() {
		return token;
	}
	private void setToken(String token) {
		this.token = token;
	}
	public String getPublishServiceUrl() {
		return publishServiceUrl;
	}
	private void setPublishServiceUrl(String publishServiceUrl) {
		this.publishServiceUrl = publishServiceUrl;
	}
	@Override
	public String toString() {
		return "CompleteReviewRequest [reviewTaskId=" + reviewTaskId + ", approved=" + approved + ", comment=" + comment
				+ ", token=" + token + "]";
	}
}
