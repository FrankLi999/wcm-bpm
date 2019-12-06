package com.bpwizard.wcm.repo.bpm.model;

public class CompleteReviewRequest {
	String reviewTaskId;
	String reviewTopic;
	String workerId;
	boolean approved;
	String comment;
	
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
