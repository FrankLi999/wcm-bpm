package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Arrays;

import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;

public class ContentAreaLayout implements HasName {
	
	String name;
	String repository;
	String workspace;
	String library;
	boolean headerEnabled = true;
	boolean footerEnabled = true;
	String theme;
	SidePane sidePane;
	int contentWidth;
	LayoutRow rows[];
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isHeaderEnabled() {
		return headerEnabled;
	}
	public void setHeaderEnabled(boolean headerEnabled) {
		this.headerEnabled = headerEnabled;
	}
	public boolean isFooterEnabled() {
		return footerEnabled;
	}
	public void setFooterEnabled(boolean footerEnabled) {
		this.footerEnabled = footerEnabled;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
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
		return "PageLayout [name=" + name + ", repository=" + repository + ", workspace=" + workspace + ", library="
				+ library + ", headerEnabled=" + headerEnabled + ", footerEnabled=" + footerEnabled + ", theme=" + theme
				+ ", sidePane=" + sidePane + ", contentWidth=" + contentWidth + ", rows=" + Arrays.toString(rows)
				+ ", toString()=" + super.toString() + "]";
	}
}
