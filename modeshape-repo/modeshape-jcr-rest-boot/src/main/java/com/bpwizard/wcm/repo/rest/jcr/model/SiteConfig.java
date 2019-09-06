package com.bpwizard.wcm.repo.rest.jcr.model;

public class SiteConfig {
	private String repository;
	private String workspace;
	private String library;
	private String rootSiteArea;
	
	private String name;
	private String colorTheme;
    private boolean customScrollbars;
    
    private PageLayout layout;
    
    
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColorTheme() {
		return colorTheme;
	}
	public void setColorTheme(String colorTheme) {
		this.colorTheme = colorTheme;
	}
	public boolean isCustomScrollbars() {
		return customScrollbars;
	}
	public void setCustomScrollbars(boolean customScrollbars) {
		this.customScrollbars = customScrollbars;
	}
	public PageLayout getLayout() {
		return layout;
	}
	public void setLayout(PageLayout layout) {
		this.layout = layout;
	}
	
	public String getRootSiteArea() {
		return rootSiteArea;
	}
	public void setRootSiteArea(String rootSiteArea) {
		this.rootSiteArea = rootSiteArea;
	}
	@Override
	public String toString() {
		return "SiteConfig [repository=" + repository + ", workspace=" + workspace + ", library=" + library
				+ ", rootSiteArea=" + rootSiteArea + ", name=" + name + ", colorTheme=" + colorTheme
				+ ", customScrollbars=" + customScrollbars + ", layout=" + layout + "]";
	}
}
