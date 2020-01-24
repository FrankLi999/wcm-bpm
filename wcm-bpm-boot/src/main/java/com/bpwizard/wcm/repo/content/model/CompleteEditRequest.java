package com.bpwizard.wcm.repo.content.model;

public class CompleteEditRequest {
	private String taskId;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Override
	public String toString() {
		return "CompleteEditRequest [taskId=" + taskId + "]";
	}
}
