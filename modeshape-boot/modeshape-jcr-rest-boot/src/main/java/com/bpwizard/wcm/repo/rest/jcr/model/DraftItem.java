package com.bpwizard.wcm.repo.rest.jcr.model;

public class DraftItem implements HasWcmAuthority {
	private String repository;
	private String wcmPath;
	private String name;
	private String id;
	private String title;
	private String description;
	private String processInstanceId;
	private String editTaskId;
	private String reviewTaskId;
	private String author;
	private WcmAuthority wcmAuthority;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getEditTaskId() {
		return editTaskId;
	}
	public void setEditTaskId(String editTaskId) {
		this.editTaskId = editTaskId;
	}
	public String getReviewTaskId() {
		return reviewTaskId;
	}
	public void setReviewTaskId(String reviewTaskId) {
		this.reviewTaskId = reviewTaskId;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public WcmAuthority getWcmAuthority() {
		return wcmAuthority;
	}
	public void setWcmAuthority(WcmAuthority wcmAuthority) {
		this.wcmAuthority = wcmAuthority;
	}
	@Override
	public String toString() {
		return "DraftItem [repository=" + repository + ", wcmPath=" + wcmPath + ", name=" + name + ", id=" + id
				+ ", title=" + title + ", description=" + description + ", processInstanceId=" + processInstanceId
				+ ", editTaskId=" + editTaskId + ", reviewTaskId=" + reviewTaskId + ", author=" + author + ", wcmAuthority=" + wcmAuthority + "]";
	}
}
