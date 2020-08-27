package com.bpwizard.wcm.repo.rest.bpm.model;

import java.util.List;

public class Resource {
	
	private String resource;
	private int resourceId;
	private List<String> permissions;
	private String title;
	
	public String getResource() {
		return resource;
	}
	
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	public int getResourceId() {
		return resourceId;
	}
	
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}
	
	public List<String> getPermissions() {
		return permissions;
	}
	
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Authorization [resource=" + resource + ", resourceId=" + resourceId + ", permissions=" + permissions
				+ ", title=" + title + "]";
	}
}
