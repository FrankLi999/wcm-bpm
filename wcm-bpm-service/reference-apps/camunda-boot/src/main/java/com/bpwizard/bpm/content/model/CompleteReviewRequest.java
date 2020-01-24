package com.bpwizard.bpm.content.model;

public class CompleteReviewRequest {
	private String reviewTaskId;
	private String reviewTopic;
	private String workerId;	
	private boolean approved;
	private String comment;
	
	public String getReviewTaskId() {
		return reviewTaskId;
	}
	
	public void setReviewTaskId(String taskId) {
		this.reviewTaskId = taskId;
	}
	
	public String getReviewTopic() {
		return reviewTopic;
	}
	
	public void setReviewTopic(String topic) {
		this.reviewTopic = topic;
	}
	
	public String getWorkerId() {
		return workerId;
	}
	
	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}
	
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
