package com.bpwizard.wcm.repo.rest.bpm.model;

public class ReviewContentItemRequest {
	private String taskName;
	private String contentId;
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	
	@Override
	public String toString() {
		return "ReviewContentItemRequest [taskName=" + taskName + ", contentId=" + contentId + "]";
	}
}
