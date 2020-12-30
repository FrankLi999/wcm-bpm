package com.bpwizard.wcm.repo.rest.bpm.model;

import java.util.List;

public class BpmLinks {
	private List<BpmLink> bpmLinks;
	private String title;
	private String description;
	public List<BpmLink> getBpmLinks() {
		return bpmLinks;
	}
	public void setBpmLinks(List<BpmLink> bpmLinks) {
		this.bpmLinks = bpmLinks;
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
	@Override
	public String toString() {
		return "BpmLinks [bpmLinks=" + bpmLinks + ", title=" + title + ", description=" + description + "]";
	}
}
