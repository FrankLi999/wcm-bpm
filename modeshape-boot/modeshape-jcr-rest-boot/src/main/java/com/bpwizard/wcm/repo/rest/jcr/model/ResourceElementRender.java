package com.bpwizard.wcm.repo.rest.jcr.model;

public class ResourceElementRender {
	private String name;
	private String source;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String toString() {
		return "ResourceElementRender [name=" + name + ", source=" + source + "]";
	}
}
