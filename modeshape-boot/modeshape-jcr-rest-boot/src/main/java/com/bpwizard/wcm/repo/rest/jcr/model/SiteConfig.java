package com.bpwizard.wcm.repo.rest.jcr.model;

import org.modeshape.jcr.api.JcrConstants;
import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.utils.WcmConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SiteConfig extends ResourceNode implements HasWcmAuthority {
	private static final long serialVersionUID = 1L;
	private String repository;
	private String workspace;
	private String library;
	private String rootSiteArea;
	
	private String name;
	private boolean animations;
	private ThemeColors themeColors;
	private String direction = "ltr";
    private boolean customScrollbars;
    
    private PageLayout layout;
    private String lockOwner;
    private WcmAuthority wcmAuthority;
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
	public ThemeColors getThemeColors() {
		return themeColors;
	}
	public void setThemeColors(ThemeColors themeColors) {
		this.themeColors = themeColors;
	}
	public boolean isCustomScrollbars() {
		return customScrollbars;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	public boolean isAnimations() {
		return animations;
	}
	public void setAnimations(boolean animations) {
		this.animations = animations;
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
	
	public WcmAuthority getWcmAuthority() {
		return wcmAuthority;
	}
	public void setWcmAuthority(WcmAuthority wcmAuthority) {
		this.wcmAuthority = wcmAuthority;
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
		ObjectNode elementsChildren = JsonUtils.createObjectNode();
		elementsNode.set(WcmConstants.JCR_JSON_NODE_CHILDREN, elementsChildren);
		
		elementsNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:system_siteConfigType_ElementFolder");
		// elementsNode.put("colorTheme", this.getColorTheme());
		elementsNode.put("rootSiteArea", this.getRootSiteArea());
		elementsNode.put("customScrollbars", this.isCustomScrollbars());
		elementsNode.put("animations", this.isAnimations());
		elementsNode.put("direction", StringUtils.hasText(this.direction) ? this.direction : "ltr");
		ObjectNode themeColors = JsonUtils.createObjectNode();
		elementsChildren.set("themeColors", themeColors);
		themeColors.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:themeColors");
		themeColors.put("main", this.themeColors.getMain());
		if (StringUtils.hasText(this.themeColors.getNavbar())) {
			themeColors.put("navbar", this.themeColors.getNavbar());
		}
		if (StringUtils.hasText(this.themeColors.getToolbar())) {
			themeColors.put("toolbar", this.themeColors.getToolbar());
		}
		if (StringUtils.hasText(this.themeColors.getFooter())) {
			themeColors.put("footer", this.themeColors.getFooter());
		}
		
		
		
		ObjectNode layout = JsonUtils.createObjectNode();
		elementsChildren.set("layout", layout);
		ObjectNode layoutChildren = JsonUtils.createObjectNode();
		layout.set(WcmConstants.JCR_JSON_NODE_CHILDREN, layoutChildren);
		
		layout.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:pageLayout");
		layout.put("bpw:title", this.layout.getTitle());
		layout.put("bpw:mode", this.layout.getMode());
		layout.put("bpw:scroll", this.layout.getScroll());
		ObjectNode navbarNode = JsonUtils.createObjectNode();
		layoutChildren.set("navbar", navbarNode);
		navbarNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:navbar");
		if (this.layout.getNavbar().getPrimaryBackground() != null) {
			navbarNode.put("primaryBackground", this.layout.getNavbar().getPrimaryBackground());	
		}
		if (this.layout.getNavbar().getSecondaryBackground() != null) {
			navbarNode.put("secondaryBackground", this.layout.getNavbar().getSecondaryBackground());
		}
		navbarNode.put("display", this.layout.getNavbar().isDisplay());
		navbarNode.put("folded", this.layout.getNavbar().isFolded());
		if (this.layout.getNavbar().getPosition() != null) {
			navbarNode.put("position", this.layout.getNavbar().getPosition());
		}
		if (this.layout.getNavbar().getVariant() != null) {
			navbarNode.put("variant", this.layout.getNavbar().getVariant());
		}
		
		if (this.layout.getNavbar().getStyle() != null) {
			navbarNode.put("style", this.layout.getNavbar().getStyle());
		}
		
		ObjectNode toolbarNode = JsonUtils.createObjectNode();
		layoutChildren.set("toolbar", toolbarNode);
		toolbarNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:toolbar");
		toolbarNode.put("customBackgroundColor", this.layout.getToolbar().isCustomBackgroundColor());
		if (this.layout.getToolbar().getBackground() != null) {
			toolbarNode.put("background", this.layout.getToolbar().getBackground());
		}
		toolbarNode.put("display", this.layout.getToolbar().isDisplay());
		if (this.layout.getToolbar().getPosition() != null) {
			toolbarNode.put("position", this.layout.getToolbar().getPosition());
		}
		if (this.layout.getToolbar().getStyle() != null) {
			toolbarNode.put("style", this.layout.getToolbar().getStyle());
		}
		ObjectNode footerNode = JsonUtils.createObjectNode();
		layoutChildren.set("footer", footerNode);
		footerNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:footer");
		footerNode.put("customBackgroundColor", this.layout.getFooter().isCustomBackgroundColor());
		if (this.layout.getFooter().getBackground() != null) {
			footerNode.put("background", this.layout.getFooter().getBackground());
		}
		footerNode.put("display", this.layout.getFooter().isDisplay());
		if (this.layout.getFooter().getPosition() != null) {
			footerNode.put("position", this.layout.getFooter().getPosition());
		}
		if (this.layout.getFooter().getStyle() != null) {
			footerNode.put("style", this.layout.getFooter().getStyle());
		}
		if (this.layout.getLeftSidePanel() != null) {
			ObjectNode leftSidePanelNode = JsonUtils.createObjectNode();
			layoutChildren.set("leftSidePanel", leftSidePanelNode);
			leftSidePanelNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:sidePanel");
			leftSidePanelNode.put("display", this.layout.getLeftSidePanel().isDisplay());
		}

		if (this.layout.getRightSidePanel() != null) {
			ObjectNode rightSidePanelNode = JsonUtils.createObjectNode();
			layoutChildren.set("rightSidePanel", rightSidePanelNode);
			rightSidePanelNode.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:sidePanel");
			rightSidePanelNode.put("display", this.layout.getRightSidePanel().isDisplay());
		}
		return jsonNode;
	}
	@Override
	public String toString() {
		return "SiteConfig [animations=" + animations + ", repository=" + repository + ", workspace=" + workspace + ", library=" + library
				+ ", rootSiteArea=" + rootSiteArea + ", name=" + name + ", themeColors=" + themeColors + ", direction="
				+ direction + ", customScrollbars=" + customScrollbars + ", layout=" + layout + ", lockOwner="
				+ lockOwner + ", wcmAuthority=" + wcmAuthority + "]";
	}
}
