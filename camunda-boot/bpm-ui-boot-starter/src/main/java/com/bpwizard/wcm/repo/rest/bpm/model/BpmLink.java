package com.bpwizard.wcm.repo.rest.bpm.model;


public class BpmLink {
	
	private String name;
	private String title;
	private String description;
	private String link;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	@Override
	public String toString() {
		return "BpmLink [name=" + name + ", title=" + title + ", description=" + description + ", link=" + link
				+ "]";
	}
}
