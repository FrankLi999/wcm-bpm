package com.bpwizard.bpm.wcm.client.model;

public class EditAsDraftRequest {
	private String author;
	private String contentId;
	private String repository;
	private String wcmPath;
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
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
	@Override
	public String toString() {
		return "EditAsDraftRequest [author=" + author + ", contentId=" + contentId + ", repository=" + repository
				+ ", wcmPath=" + wcmPath + "]";
	}
}


