package com.bpwizard.bpm.content.model;

public class ReviewContentItemRequest {
	private String reviewTopic;
	private String workerId;
	private String contentId;
	
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
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	@Override
	public String toString() {
		return "ReviewContentItemRequest [reviewTopic=" + reviewTopic + ", workerId=" + workerId + ", contentId="
				+ contentId + "]";
	}
}
