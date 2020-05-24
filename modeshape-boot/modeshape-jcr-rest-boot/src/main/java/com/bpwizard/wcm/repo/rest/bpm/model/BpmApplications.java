package com.bpwizard.wcm.repo.rest.bpm.model;

import java.util.List;

public class BpmApplications {
	
	private List<BpmApplication> bpmApplications;
	private String title;
	private String description;

	public List<BpmApplication> getBpmApplications() {
		return bpmApplications;
	}

	public void setBpmApplications(List<BpmApplication> bpmApplications) {
		this.bpmApplications = bpmApplications;
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
		return "BpmApplications [bpmApplications=" + bpmApplications + ", title=" + title + ", description="
				+ description + "]";
	}
}
