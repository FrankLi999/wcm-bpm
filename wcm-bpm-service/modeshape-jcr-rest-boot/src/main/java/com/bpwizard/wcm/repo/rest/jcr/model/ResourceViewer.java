package com.bpwizard.wcm.repo.rest.jcr.model;

public class ResourceViewer {
	private String renderTemplate;
	private String contentPath;
	
	public String getRenderTemplate() {
		return renderTemplate;
	}

	public void setRenderTemplate(String renderTemplate) {
		this.renderTemplate = renderTemplate;
	}

	public String getContentPath() {
		return contentPath;
	}

	public void setContentPath(String contentPath) {
		this.contentPath = contentPath;
	}

	@Override
	public String toString() {
		return "ResourceViewer [renderTemplate=" + renderTemplate + ", contentPath=" + contentPath + "]";
	}
}
