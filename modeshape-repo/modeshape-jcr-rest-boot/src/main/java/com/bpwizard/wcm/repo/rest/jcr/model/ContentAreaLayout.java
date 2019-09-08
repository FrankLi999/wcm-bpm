package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;

public class ContentAreaLayout implements HasName {
	
	String name;
	String repository;
	String workspace;
	String library;
	int contentWidth;
	SidePane sidePane;
	LayoutRow rows[];
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SidePane getSidePane() {
		return sidePane;
	}
	public void setSidePane(SidePane sidePane) {
		this.sidePane = sidePane;
	}
	public LayoutRow[] getRows() {
		return rows;
	}
	public void setRows(LayoutRow[] rows) {
		this.rows = rows;
	}
	public int getContentWidth() {
		return contentWidth;
	}
	public void setContentWidth(int contentWidth) {
		this.contentWidth = contentWidth;
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
		return "ContentAreaLayout [name=" + name + ", repository=" + repository + ", workspace=" + workspace
				+ ", library=" + library + ", sidePane=" + sidePane + ", contentWidth=" + contentWidth + ", rows="
				+ Arrays.toString(rows) + "]";
	}
}
