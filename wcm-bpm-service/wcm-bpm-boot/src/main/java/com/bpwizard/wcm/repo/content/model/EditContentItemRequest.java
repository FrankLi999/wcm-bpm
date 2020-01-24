package com.bpwizard.wcm.repo.content.model;

public class EditContentItemRequest {
	private String contentId;
	private String taskName;
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	@Override
	public String toString() {
		return "CompleteEditRequest [contentId=" + contentId + ", taskName=" + taskName + "]";
	}
}
