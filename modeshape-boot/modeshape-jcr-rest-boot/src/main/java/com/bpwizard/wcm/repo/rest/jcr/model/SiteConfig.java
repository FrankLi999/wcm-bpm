package com.bpwizard.wcm.repo.rest.jcr.model;

import org.modeshape.jcr.api.JcrConstants;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SiteConfig extends ResourceNode {
	private static final long serialVersionUID = 1L;
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
		ObjectNode children = JsonUtils.createObjectNode();
		
		jsonNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, children);
		jsonNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:system_siteConfigType");
		
		ObjectNode propertiesNode = JsonUtils.createObjectNode();
		children.set(WcmConstants.WCM_ITEM_PROPERTIES, propertiesNode);
		propertiesNode.put(JcrConstants.JCR_PRIMARY_TYPE, WcmConstants.JCR_TYPE_PROPERTY_FOLDER);
		propertiesNode.put("bpw:name", this.getName());
//		propertiesNode.put("bpw:title", this.getTitle());
//		propertiesNode.put("bpw:description", this.getDescription());
		
		ObjectNode elementsNode = JsonUtils.createObjectNode();
		children.set(WcmConstants.WCM_ITEM_ELEMENTS, elementsNode);
		elementsNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:system_siteConfigType_ElementFolder");
		elementsNode.put("colorTheme", this.getColorTheme());
		elementsNode.put("rootSiteArea", this.getRootSiteArea());
		elementsNode.put("customScrollbars", this.isCustomScrollbars());
		
		ObjectNode layout = JsonUtils.createObjectNode();
		elementsNode.set("layout", layout);
		ObjectNode layoutChildren = JsonUtils.createObjectNode();
		layout.set(WcmConstants.JCR_JSON_NODE_CHILDREN, layoutChildren);
		
		layout.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:pageLayout");
		layout.put("bpw:style", this.layout.getStyle());
		layout.put("bpw:width", this.layout.getWidth());
		
		ObjectNode navbarNode = JsonUtils.createObjectNode();
		layoutChildren.set("navbar", navbarNode);
		navbarNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:navbar");
		if (this.layout.getNavbar().getPrimaryBackground() != null) {
			navbarNode.put("primaryBackground", this.layout.getNavbar().getPrimaryBackground());	
		}
		if (this.layout.getNavbar().getSecondaryBackground() != null) {
			navbarNode.put("secondaryBackground", this.layout.getNavbar().getSecondaryBackground());
		}
		navbarNode.put("hidden", this.layout.getNavbar().isHidden());
		navbarNode.put("folded", this.layout.getNavbar().isFolded());
		if (this.layout.getNavbar().getPosition() != null) {
			navbarNode.put("position", this.layout.getNavbar().getPosition());
		}
		if (this.layout.getNavbar().getVariant() != null) {
			navbarNode.put("variant", this.layout.getNavbar().getVariant());
		}
		
		ObjectNode toolbarNode = JsonUtils.createObjectNode();
		layoutChildren.set("toolbar", toolbarNode);
		toolbarNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:toolbar");
		toolbarNode.put("customBackgroundColor", this.layout.getToolbar().isCustomBackgroundColor());
		if (this.layout.getToolbar().getBackground() != null) {
			toolbarNode.put("background", this.layout.getToolbar().getBackground());
		}
		toolbarNode.put("hidden", this.layout.getToolbar().isHidden());
		if (this.layout.getToolbar().getPosition() != null) {
			toolbarNode.put("position", this.layout.getToolbar().getPosition());
		}
		
		ObjectNode footerNode = JsonUtils.createObjectNode();
		layoutChildren.set("footer", footerNode);
		footerNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:footer");
		footerNode.put("customBackgroundColor", this.layout.getFooter().isCustomBackgroundColor());
		if (this.layout.getFooter().getBackground() != null) {
			footerNode.put("background", this.layout.getFooter().getBackground());
		}
		footerNode.put("hidden", this.layout.getFooter().isHidden());
		if (this.layout.getFooter().getPosition() != null) {
			footerNode.put("position", this.layout.getFooter().getPosition());
		}

		ObjectNode sidePanelNode = JsonUtils.createObjectNode();
		layoutChildren.set("sidePanel", sidePanelNode);
		sidePanelNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:sidePanel");
		sidePanelNode.put("hidden", this.layout.getSidePanel().isHidden());
		if (this.layout.getSidePanel().getPosition() != null) {
			sidePanelNode.put("position", this.layout.getSidePanel().getPosition());
		}

		return jsonNode;
	}
	@Override
	public String toString() {
		return "SiteConfig [repository=" + repository + ", workspace=" + workspace + ", library=" + library
				+ ", rootSiteArea=" + rootSiteArea + ", name=" + name + ", colorTheme=" + colorTheme
				+ ", customScrollbars=" + customScrollbars + ", layout=" + layout + ", LockOwner=" + lockOwner + "]";
	}
}
