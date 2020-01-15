package com.bpwizard.wcm.repo.rest.jcr.model;

public class WcmOperation {
	private String jcrType;
	private String resourceName;
	private String operation;
	private String defaultTitle;
	private String condition;
	private String defaultIcon;
	
	
	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getDefaultTitle() {
		return defaultTitle;
	}

	public void setDefaultTitle(String defaultTitle) {
		this.defaultTitle = defaultTitle;
	}

	public String getDefaultIcon() {
		return defaultIcon;
	}

	public void setDefaultIcon(String defaultIcon) {
		this.defaultIcon = defaultIcon;
	}

	public String getJcrType() {
		return jcrType;
	}

	public void setJcrType(String jcrType) {
		this.jcrType = jcrType;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	@Override
	public String toString() {
		return "WcmOperation [jcrType=" + jcrType + ", resourceName=" + resourceName + ", operation=" + operation
				+ ", defaultTitle=" + defaultTitle + ", condition=" + condition + ", defaultIcon=" + defaultIcon + "]";
	}
}
