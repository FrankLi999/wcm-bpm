package com.bpwizard.wcm.repo.rest.jcr.model;

public class ResourceElementRender {
	private String name;
	private String source;
	private String body;
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

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "ResourceElementRender [name=" + name + ", source=" + source + ", body=" + body + "]";
	}
}
