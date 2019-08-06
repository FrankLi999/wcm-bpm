package com.bpwizard.wcm.repo.rest.jcr.model;

public class ResourceViewer {
	private String renderTemplate;

	public String getRenderTemplate() {
		return renderTemplate;
	}

	public void setRenderTemplate(String renderTemplate) {
		this.renderTemplate = renderTemplate;
	}

	@Override
	public String toString() {
		return "ResourceViewer [renderTemplate=" + renderTemplate + "]";
	}
}
