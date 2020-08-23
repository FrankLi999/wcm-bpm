package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;
import java.util.Map;

public class ResourceViewer {
	private String renderTemplate;
	private String contentPath[];
	private String title = "rv";
	private String contentParameter;
	private Map<String, String> parameterValues;
	
	public String getRenderTemplate() {
		return renderTemplate;
	}

	public void setRenderTemplate(String renderTemplate) {
		this.renderTemplate = renderTemplate;
	}

	public String[] getContentPath() {
		return contentPath;
	}

	public void setContentPath(String contentPath[]) {
		this.contentPath = contentPath;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public Map<String, String> getParameterValues() {
		return parameterValues;
	}

	public void setParameterValues(Map<String, String> parameterValues) {
		this.parameterValues = parameterValues;
	}

	public String getContentParameter() {
		return contentParameter;
	}

	public void setContentParameter(String contentParameter) {
		this.contentParameter = contentParameter;
	}

	@Override
	public String toString() {
		return "ResourceViewer [renderTemplate=" + renderTemplate + ", contentPath=" + Arrays.toString(contentPath)
				+ ", title=" + title + ", contentParameter=" + contentParameter + ", parameterValues=" + parameterValues
				+ "]";
	}
}
