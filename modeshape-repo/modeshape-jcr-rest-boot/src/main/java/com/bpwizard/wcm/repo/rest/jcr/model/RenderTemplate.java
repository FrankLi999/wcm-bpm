package com.bpwizard.wcm.repo.rest.jcr.model;

import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;

public class RenderTemplate implements HasName {
	private String repository;
	private String workspace;
	private String library;
	private String name;
	private String title;
	private String description;
	private String code;
	private String preloop;
	private String postloop;
	private int maxEntries;
	private String note;
	private boolean isQuery;
	private String resourceName;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPreloop() {
		return preloop;
	}
	public void setPreloop(String preloop) {
		this.preloop = preloop;
	}
	public String getPostloop() {
		return postloop;
	}
	public void setPostloop(String postloop) {
		this.postloop = postloop;
	}
	public int getMaxEntries() {
		return maxEntries;
	}
	public void setMaxEntries(int maxEntries) {
		this.maxEntries = maxEntries;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public boolean isQuery() {
		return isQuery;
	}
	public void setQuery(boolean isQuery) {
		this.isQuery = isQuery;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
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
	public String getLibrary() {
		return library;
	}
	public void setLibrary(String library) {
		this.library = library;
	}
	@Override
	public String toString() {
		return "RenderTemplate [repository=" + repository + ", workspace=" + workspace + ", library=" + library
				+ ", name=" + name + ", title=" + title + ", description=" + description + ", code=" + code
				+ ", preloop=" + preloop + ", postloop=" + postloop + ", maxEntries=" + maxEntries + ", note=" + note
				+ ", isQuery=" + isQuery + ", resourceName=" + resourceName + "]";
	}
	
}
