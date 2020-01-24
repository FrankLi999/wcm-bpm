package com.bpwizard.bpm.content.model;

public class CompleteEditRequest {
	private String editTaskId;
	private String editTopic;
	private String workerId;
	
	public String getEditTaskId() {
		return editTaskId;
	}
	public void setEditTaskId(String taskId) {
		this.editTaskId = taskId;
	}
	public String getEditTopic() {
		return editTopic;
	}
	public void setEditTopic(String topic) {
		this.editTopic = topic;
	}
	public String getWorkerId() {
		return workerId;
	}
	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}
}
