package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class WcmItems {
	
	private String repository;
	private String workspace;
	private String wcmPaths[];

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
	
	public String[] getWcmPaths() {
		return wcmPaths;
	}
	
	public void setWcmPaths(String[] wcmPaths) {
		this.wcmPaths = wcmPaths;
	}
	
	@Override
	public String toString() {
		return "WcmItems [repository=" + repository + ", workspace=" + workspace + ", wcmPaths="
				+ Arrays.toString(wcmPaths) + "]";
	}
}
