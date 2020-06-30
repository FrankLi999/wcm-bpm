package com.bpwizard.wcm.repo.rest.jcr.model;

public class DraftItem implements HasWcmAuthority {
	private String repository;
	private String wcmPath;
	private String name;
	private String id;
	private String title;
	private String description;
	private String processInstanceId;
	private String editor;
	private String reviewer;
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
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getReviewer() {
		return reviewer;
	}
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
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
				+ ", editor=" + editor + ", reviewer=" + reviewer + ", author=" + author + ", wcmAuthority="
				+ wcmAuthority + "]";
	}
}
