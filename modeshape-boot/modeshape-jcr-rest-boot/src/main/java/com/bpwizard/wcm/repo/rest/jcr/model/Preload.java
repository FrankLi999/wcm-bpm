package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

public class Preload {
	public enum PreloadType {
		LIBRARY,
		ITEMS,
		QUERY,
		CND
	}
	String repository;
	String workspace[];
	String preloadType;
	String path;
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
	public String[] getWorkspace() {
		return workspace;
	}
	public void setWorkspace(String[] workspace) {
		this.workspace = workspace;
	}
	public String getPreloadType() {
		return preloadType;
	}
	public void setPreloadType(String preloadType) {
		this.preloadType = preloadType;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	@Override
	public String toString() {
		return "Preload [repository=" + repository + ", workspace=" + Arrays.toString(workspace) + ", preloadType="
				+ preloadType + ", path=" + path + "]";
	}
}
