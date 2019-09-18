package com.bpwizard.wcm.repo.rest.jcr.model;

public class WcmLibrary {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "WcmLibrary [name=" + name + "]";
	}
}
