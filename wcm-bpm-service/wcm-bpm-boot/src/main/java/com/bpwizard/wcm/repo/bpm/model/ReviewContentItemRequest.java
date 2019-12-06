package com.bpwizard.wcm.repo.bpm.model;

public class ReviewContentItemRequest {
	private String reviewTopic;
	private String workerId;
	
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
	
}
