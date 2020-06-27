package com.bpwizard.wcm.repo.rest.bpm.model;

public class CompleteReviewRequest {
	private String reviewTaskId;
	private boolean approved;
	private String comment;
	
	public String getReviewTaskId() {
		return reviewTaskId;
	}
	
	public void setReviewTaskId(String taskId) {
		this.reviewTaskId = taskId;
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

	@Override
	public String toString() {
		return "CompleteReviewRequest [reviewTaskId=" + reviewTaskId + ", approved=" + approved + ", comment=" + comment
				+ "]";
	}
}
