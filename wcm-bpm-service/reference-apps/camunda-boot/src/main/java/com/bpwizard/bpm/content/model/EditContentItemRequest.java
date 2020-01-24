package com.bpwizard.bpm.content.model;

public class EditContentItemRequest {
	private String editTopic;
	private String contentId;
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
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	@Override
	public String toString() {
		return "EditContentItemRequest [editTopic=" + editTopic + ", contentId=" + contentId + ", workerId=" + workerId
				+ "]";
	}
}
