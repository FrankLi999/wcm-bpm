package com.bpwizard.wcm.repo.rest.bpm.model;

public class CompleteEditRequest {
	private String taskId;
	public static CompleteEditRequest createCompleteEditRequest(String taskId) {
		CompleteEditRequest completeEditRequest = new CompleteEditRequest();
		completeEditRequest.setTaskId(taskId);
		return completeEditRequest;
	}
	

	public String getTaskId() {
		return taskId;
	}

	private void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Override
	public String toString() {
		return "CompleteEditRequest [taskId=" + taskId + "]";
	}
}
