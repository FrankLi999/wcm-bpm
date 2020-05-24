package com.bpwizard.wcm.repo.rest.bpm.model;

public class BpmApplication {
	
	private String name;
	private String title;
	private String description;
	private String link;
	private String imageUrl;
	
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
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	@Override
	public String toString() {
		return "BpmApplication [name=" + name + ", title=" + title + ", description=" + description + ", link=" + link
				+ ", imageUrl=" + imageUrl + "]";
	}
}
