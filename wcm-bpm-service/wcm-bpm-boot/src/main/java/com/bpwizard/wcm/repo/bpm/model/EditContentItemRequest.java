package com.bpwizard.wcm.repo.bpm.model;

public class EditContentItemRequest {
	private String editTopic;
	private String workerId;
	
	
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
