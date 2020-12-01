package com.bpwizard.bpm.wcm.client.model;

public class ClaimReivewTaskReuqest {
	private String repository;
	private String wcmPath;
	private String contentId;
	private String processInstanceId;
	
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
	public String getWcmPath() {
		return wcmPath;
	}
	public void setWcmPath(String wcmPath) {
		this.wcmPath = wcmPath;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	@Override
	public String toString() {
		return "ClaimReivewTaskReuqest [repository=" + repository + ", wcmPath=" + wcmPath + ", contentId=" + contentId
				+ ", processInstanceId=" + processInstanceId + "]";
	}
}
