package com.bpwizard.wcm.repo.bpm.model;

public class CompleteEditRequest {
	String editTaskId;
	String editTopic;
	String workerId;
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
