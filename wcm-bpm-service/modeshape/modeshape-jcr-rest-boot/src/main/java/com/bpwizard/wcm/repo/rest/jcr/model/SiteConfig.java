package com.bpwizard.wcm.repo.rest.jcr.model;

import org.modeshape.jcr.api.JcrConstants;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SiteConfig {
	private String repository;
	private String workspace;
	private String library;
	private String rootSiteArea;
	
	private String name;
	private String colorTheme;
    private boolean customScrollbars;
    
    private PageLayout layout;
    private String lockOwner;
    
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
	public String getLockOwner() {
		return lockOwner;
	}
	public void setLockOwner(String lockOwner) {
		this.lockOwner = lockOwner;
	}
	public JsonNode toJson() {
		ObjectNode jsonNode = JsonUtils.createObjectNode();
		ObjectNode properties = JsonUtils.createObjectNode();
		ObjectNode children = JsonUtils.createObjectNode();
		
		jsonNode.set("children", children);
		jsonNode.set("properties", properties);
		
		properties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:siteConfig");
		properties.put("bpw:name", this.getName());
		properties.put("bpw:colorTheme", this.getColorTheme());
		properties.put("bpw:rootSiteArea", this.getRootSiteArea());
		properties.put("bpw:customScrollbars", this.isCustomScrollbars());
		

		ObjectNode layout = JsonUtils.createObjectNode();
		children.set("layout", layout);
		ObjectNode layoutProperties = JsonUtils.createObjectNode();
		ObjectNode layoutChildren = JsonUtils.createObjectNode();
		layout.set("children", layoutChildren);
		layout.set("properties", layoutChildren);
		
		layoutProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:pageLayout");
		layoutProperties.put("bpw:style", this.layout.getStyle());
		layoutProperties.put("bpw:width", this.layout.getWidth());
		
		ObjectNode navbarNode = JsonUtils.createObjectNode();
		ObjectNode navbarNodeProperties = JsonUtils.createObjectNode();
		layoutChildren.set("navbar", navbarNode);
		navbarNode.set("properties", navbarNodeProperties);
		navbarNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:navbar");
		navbarNodeProperties.put("primaryBackground", this.layout.getNavbar().getPrimaryBackground());
		navbarNodeProperties.put("secondaryBackground", this.layout.getNavbar().getSecondaryBackground());
		navbarNodeProperties.put("hidden", this.layout.getNavbar().isHidden());
		navbarNodeProperties.put("folded", this.layout.getNavbar().isFolded());
		navbarNodeProperties.put("position", this.layout.getNavbar().getPosition());
		navbarNodeProperties.put("variant", this.layout.getNavbar().getVariant());
		
		ObjectNode toolbarNode = JsonUtils.createObjectNode();
		layoutChildren.set("toolbar", toolbarNode);
		ObjectNode toolbarNodeProperties = JsonUtils.createObjectNode();
		toolbarNode.set("properties", toolbarNodeProperties);
		toolbarNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:toolbar");
		toolbarNodeProperties.put("customBackgroundColor", this.layout.getToolbar().isCustomBackgroundColor());
		toolbarNodeProperties.put("background", this.layout.getToolbar().getBackground());
		toolbarNodeProperties.put("hidden", this.layout.getToolbar().isHidden());
		toolbarNodeProperties.put("position", this.layout.getToolbar().getPosition());
		
		ObjectNode footerNode = JsonUtils.createObjectNode();
		layoutChildren.set("footer", footerNode);
		ObjectNode footerNodeProperties = JsonUtils.createObjectNode();
		footerNode.set("properties", footerNodeProperties);
		footerNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:footer");
		footerNodeProperties.put("customBackgroundColor", this.layout.getFooter().isCustomBackgroundColor());
		footerNodeProperties.put("background", this.layout.getFooter().getBackground());
		footerNodeProperties.put("hidden", this.layout.getFooter().isHidden());
		footerNodeProperties.put("position", this.layout.getFooter().getPosition());

		ObjectNode sidePanelNode = JsonUtils.createObjectNode();
		layoutChildren.set("sidePanel", sidePanelNode);
		ObjectNode sidePanelNodeProperties = JsonUtils.createObjectNode();
		sidePanelNode.set("properties", sidePanelNodeProperties);
		sidePanelNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:sidePanel");
		sidePanelNodeProperties.put("hidden", this.layout.getSidePanel().isHidden());
		sidePanelNodeProperties.put("position", this.layout.getSidePanel().getPosition());

		return jsonNode;
	}
	@Override
	public String toString() {
		return "SiteConfig [repository=" + repository + ", workspace=" + workspace + ", library=" + library
				+ ", rootSiteArea=" + rootSiteArea + ", name=" + name + ", colorTheme=" + colorTheme
				+ ", customScrollbars=" + customScrollbars + ", layout=" + layout + ", LockOwner=" + lockOwner + "]";
	}
}
