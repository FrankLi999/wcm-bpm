package com.bpwizard.bpm.content.model;

import java.io.Serializable;

public class ReviewTask implements Serializable {
	private static final long serialVersionUID = -2384105874826504993L;
	private String activityId;
	private String topic;
	private String repository;
	private String workspace;
	private String contentPath;
	private String contentId;
	
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
	public String getWorkspace() {
		return workspace;
	}
	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}
	public String getContentPath() {
		return contentPath;
	}
	public void setContentPath(String contentPath) {
		this.contentPath = contentPath;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	@Override
	public String toString() {
		return "ReviewTask [activityId=" + activityId + ", topic=" + topic + ", repository=" + repository
				+ ", workspace=" + workspace + ", contentPath=" + contentPath + ", contentId=" + contentId + "]";
	}
}
