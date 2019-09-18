package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Map;

public class ContentItem {
	private String repository;
	private String workspace;
	private String nodePath;
	private String authoringTemplate;
	
	private Map<String, String> contentElements;

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

	public String getNodePath() {
		return nodePath;
	}

	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}

	public String getAuthoringTemplate() {
		return authoringTemplate;
	}

	public void setAuthoringTemplate(String authoringTemplate) {
		this.authoringTemplate = authoringTemplate;
	}


	public Map<String, String> getContentElements() {
		return contentElements;
	}

	public void setContentElements(Map<String, String> contentElements) {
		this.contentElements = contentElements;
	}

	@Override
	public String toString() {
		return "ContentItem [repository=" + repository + ", workspace=" + workspace + ", nodePath=" + nodePath
				+ ", authoringTemplate=" + authoringTemplate + ", contentElements=" + contentElements + "]";
	}

	
}
