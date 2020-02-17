package com.bpwizard.wcm.repo.rest.jcr.model;

import java.util.Map;

import org.modeshape.jcr.api.JcrConstants;
import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.JsonUtils;
import com.bpwizard.wcm.repo.rest.modeshape.model.HasName;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SiteArea implements HasName {

	private String repository;
	private String workspace;
	private String nodePath;
	private String name;
	private SiteAreaLayout siteAreaLayout;
	private String lockOwner;
	
	private Map<String, String> elements;
	private Map<String, String> properties;
	
//	private String title;
//	private String description;
//	private String url;
//	private int sorderOrder;
//	private String friendlyURL;
//	private boolean showOnMenu;
//	private String defaultContent;
//	private String allowedFileExtension;
//	private String allowedArtifactTypes;
//	
//	private boolean securePage;
//	private String siteConfig;
//	private String contentAreaLayout;
//	private int cacheTTL;
//
//	private String navigationId;
//	private String navigationType;
//	private String function;
//	private String translate;
//	private String icon;
//	private String classes;
//	private boolean exactMatch;
//	private boolean externalUrl;
//	private boolean openInNewTab;
//
	private keyValues metadata;
	private SearchData searchData;
	private NavigationBadge badge;

	
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
	public String getNodePath() {
		return nodePath;
	}
	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public SiteAreaLayout getSiteAreaLayout() {
		return siteAreaLayout;
	}
	public void setSiteAreaLayout(SiteAreaLayout siteAreaLayout) {
		this.siteAreaLayout = siteAreaLayout;
	}
	
	public String getLockOwner() {
		return lockOwner;
	}
	public void setLockOwner(String lockOwner) {
		this.lockOwner = lockOwner;
	}	
	
	public Map<String, String> getElements() {
		return elements;
	}
	
	public void setElements(Map<String, String> elements) {
		this.elements = elements;
	}
	
	public Map<String, String> getProperties() {
		return properties;
	}
	
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

//	public String getTitle() {
//		return title;
//	}
//	public void setTitle(String title) {
//		this.title = title;
//	}
//	public String getDescription() {
//		return description;
//	}
//	public void setDescription(String description) {
//		this.description = description;
//	}
//	public String getUrl() {
//		return url;
//	}
//	public void setUrl(String url) {
//		this.url = url;
//	}
//	public int getSorderOrder() {
//		return sorderOrder;
//	}
//	public void setSorderOrder(int sorderOrder) {
//		this.sorderOrder = sorderOrder;
//	}
//	public String getFriendlyURL() {
//		return friendlyURL;
//	}
//	public void setFriendlyURL(String friendlyURL) {
//		this.friendlyURL = friendlyURL;
//	}
//	public boolean isShowOnMenu() {
//		return showOnMenu;
//	}
//	public void setShowOnMenu(boolean showOnMenu) {
//		this.showOnMenu = showOnMenu;
//	}
//	public String getDefaultContent() {
//		return defaultContent;
//	}
//	public void setDefaultContent(String defaultContent) {
//		this.defaultContent = defaultContent;
//	}
//	public String getAllowedFileExtension() {
//		return allowedFileExtension;
//	}
//	public void setAllowedFileExtension(String allowedFileExtension) {
//		this.allowedFileExtension = allowedFileExtension;
//	}
//	public String getAllowedArtifactTypes() {
//		return allowedArtifactTypes;
//	}
//	public void setAllowedArtifactTypes(String allowedArtifactTypes) {
//		this.allowedArtifactTypes = allowedArtifactTypes;
//	}
//	public boolean isSecurePage() {
//		return securePage;
//	}
//	public void setSecurePage(boolean securePage) {
//		this.securePage = securePage;
//	}
//	public String getContentAreaLayout() {
//		return contentAreaLayout;
//	}
//	public void setContentAreaLayout(String contentAreaLayout) {
//		this.contentAreaLayout = contentAreaLayout;
//	}
//	public int getCacheTTL() {
//		return cacheTTL;
//	}
//	public void setCacheTTL(int cacheTTL) {
//		this.cacheTTL = cacheTTL;
//	}
	public keyValues getMetadata() {
		return metadata;
	}
	public void setMetadata(keyValues metadata) {
		this.metadata = metadata;
	}
	public SearchData getSearchData() {
		return searchData;
	}
	public void setSearchData(SearchData searchData) {
		this.searchData = searchData;
	}
//	public String getNavigationId() {
//		return navigationId;
//	}
//	public void setNavigationId(String navigationId) {
//		this.navigationId = navigationId;
//	}
//	public String getNavigationType() {
//		return navigationType;
//	}
//	public void setNavigationType(String navigationType) {
//		this.navigationType = navigationType;
//	}
//	public String getFunction() {
//		return function;
//	}
//	public void setFunction(String function) {
//		this.function = function;
//	}
//	public String getTranslate() {
//		return translate;
//	}
//	public void setTranslate(String translate) {
//		this.translate = translate;
//	}
//	public String getIcon() {
//		return icon;
//	}
//	public void setIcon(String icon) {
//		this.icon = icon;
//	}
//	public String getClasses() {
//		return classes;
//	}
//	public void setClasses(String classes) {
//		this.classes = classes;
//	}
//	public boolean isExactMatch() {
//		return exactMatch;
//	}
//	public void setExactMatch(boolean exactMatch) {
//		this.exactMatch = exactMatch;
//	}
//	public boolean isExternalUrl() {
//		return externalUrl;
//	}
//	public void setExternalUrl(boolean externalUrl) {
//		this.externalUrl = externalUrl;
//	}
//	public boolean isOpenInNewTab() {
//		return openInNewTab;
//	}
//	public void setOpenInNewTab(boolean openInNewTab) {
//		this.openInNewTab = openInNewTab;
//	}
	public NavigationBadge getBadge() {
		return badge;
	}
	public void setBadge(NavigationBadge badge) {
		this.badge = badge;
	}
//	
//	public String getSiteConfig() {
//		return siteConfig;
//	}
//	public void setSiteConfig(String siteConfig) {
//		this.siteConfig = siteConfig;
//	}
	
	public JsonNode toJson() {
		ObjectNode jsonNode = JsonUtils.createObjectNode();
		ObjectNode properties = JsonUtils.createObjectNode();
		ObjectNode children = JsonUtils.createObjectNode();
		
		jsonNode.set("children", children);
		jsonNode.set("properties", properties);
		properties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:siteArea");
		
		properties.put("bpw:name", this.getName());
		
		for (String key : this.getElements().keySet()) {
			properties.put(String.format("bpw:%s", key), this.getElements().get(key));
		}
		String title = StringUtils.hasText(this.getProperties().get("title")) ? this.getProperties().get("title") : this.getName();
		properties.put("bpw:title", title);
		String url = StringUtils.hasText(this.getProperties().get("url")) ? this.getProperties().get("url") : this.getName();
		properties.put("bpw:url", url);
		for (String prop : this.getProperties().keySet()) {
			if ("url".equals(prop) || "title".equals(prop)) {
				continue;
			} else if ("allowedArtifactTypes".equals(prop)) {
				String allowedArtifactTypes = this.getProperties().get("allowedArtifactTypes");
				String[] artifacts = allowedArtifactTypes.split(",");
				ArrayNode artifactArray = JsonUtils.creatArrayNode();
				for (String value : artifacts) {
					artifactArray.add(value);
				}
				properties.set("bpw:allowedArtifactTypes", artifactArray);
			} else if ("allowedFileExtension".equals(prop)) {
				String allowedFileExtension = this.getProperties().get("allowedFileExtension");
				String[] artifacts = allowedFileExtension.split(",");
				ArrayNode artifactArray = JsonUtils.creatArrayNode();
				for (String value : artifacts) {
					artifactArray.add(value);
				}
				properties.set("bpw:allowedFileExtension", artifactArray);
			} else {
			
				//bpw:description, bpw:friendlyURL, bpw:sorderOrder, bpw:showOnMenu, bpw:defaultContent
				// bpw:contentAreaLayout, bpw:siteConfig, bpw:securePage, bpw:cacheTTL
				// bpw:navigationId, bpw:navigationType, bpw:translate, bpw:function, bpw:icon
				// bpw:classes, bpw:exactMatch, bpw:externalUrl, bpw:openInNewTab
				properties.put(String.format("bpw:%s", prop), this.getProperties().get(prop));
			}
		}
		
		if (this.getBadge() != null) {
			NavigationBadge badge = this.getBadge();
			ObjectNode badgeNode = JsonUtils.createObjectNode();
			ObjectNode badgeNodeProperties = JsonUtils.createObjectNode();
			children.set("bpw:badge", badgeNode);
			badgeNode.set("properties", badgeNodeProperties);
			badgeNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:navigationBadge");
				
			if (StringUtils.hasText(badge.getTitle())) {
				badgeNodeProperties.put("bpw:title", badge.getTitle());
			}
			if (StringUtils.hasText(badge.getTranslate())) {
				badgeNodeProperties.put("bpw:translate", badge.getTranslate());
			}
			if (StringUtils.hasText(badge.getBg())) {
				badgeNodeProperties.put("bpw:bg", badge.getBg());
			}
			if (StringUtils.hasText(badge.getFg())) {
				badgeNodeProperties.put("bpw:fg", badge.getFg());
			}
		}
		
		if (this.getMetadata() != null) {
			
			ObjectNode metaDataNode = JsonUtils.createObjectNode();
			ObjectNode metaDataNodeProperties = JsonUtils.createObjectNode();
			ObjectNode metaDataNodeChildren = JsonUtils.createObjectNode();
			children.set("bpw:metaData", metaDataNode);
			metaDataNode.set("properties", metaDataNodeProperties);
			metaDataNode.set("children", metaDataNodeChildren);
			metaDataNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:keyValues");
			int count = 0;
			keyValues metadata = (keyValues)this.getMetadata();
			for (KeyValue keyValue: metadata.getKeyValues()) {
				
				ObjectNode kvNode = JsonUtils.createObjectNode();
				ObjectNode kvNodeProperties = JsonUtils.createObjectNode();
				ObjectNode kvNodeChildren = JsonUtils.createObjectNode();
				metaDataNodeChildren.set("kv" + count++, kvNode);
				kvNode.set("properties", kvNodeProperties);
				kvNode.set("children", kvNodeChildren);
				kvNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:keyValue");
				kvNodeProperties.put("bpw:name", keyValue.getName());
				kvNodeProperties.put("bpw:value", keyValue.getValue());
			}
		} 
		if (this.getSearchData() != null) {	
			ObjectNode searchDataDataNode = JsonUtils.createObjectNode();
			ObjectNode searchDataDataNodeProperties = JsonUtils.createObjectNode();
			children.set("bpw:searchData", searchDataDataNode);
			searchDataDataNode.set("properties", searchDataDataNodeProperties);
			searchDataDataNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:pageSearchData");
			SearchData searchData = (SearchData) this.getSearchData();
			if (StringUtils.hasText(searchData.getDescription())) {
				searchDataDataNodeProperties.put("description", searchData.getDescription());
			}
			if (searchData.getKeywords() != null) {
				String[] keywords = searchData.getKeywords();
				ArrayNode keywordArray = JsonUtils.creatArrayNode();
				for (String keyword : keywords) {
					keywordArray.add(keyword);
				}				
				searchDataDataNodeProperties.set("keywords", keywordArray);
			}	
		}
		
		if (this.getSiteAreaLayout() != null) {
			ObjectNode siteAreaLayoutNode = JsonUtils.createObjectNode();
			ObjectNode siteAreaLayoutNodeProperties = JsonUtils.createObjectNode();
			ObjectNode siteAreaLayoutNodeChildren = JsonUtils.createObjectNode();
			children.set("siteAreaLayout", siteAreaLayoutNode);
			siteAreaLayoutNode.set("properties", siteAreaLayoutNodeProperties);
			siteAreaLayoutNode.set("children", siteAreaLayoutNodeChildren);
			siteAreaLayoutNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:siteAreaLayout");
			
			
			ObjectNode sidePaneNode = JsonUtils.createObjectNode();
			ObjectNode sidePaneNodeProperties = JsonUtils.createObjectNode();
			ObjectNode sidePaneNodeChildren = JsonUtils.createObjectNode();
			siteAreaLayoutNodeChildren.set("sidePane", siteAreaLayoutNode);
			sidePaneNode.set("properties", sidePaneNodeProperties);
			sidePaneNode.set("children", sidePaneNodeChildren);
			sidePaneNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:contentAreaSidePanel");
			
			sidePaneNodeProperties.put("bpw:contentWidth", this.getSiteAreaLayout().getContentWidth());
			
			
			this.addSidePaneNode(sidePaneNodeChildren, sidePaneNodeProperties, this.getSiteAreaLayout().getSidePane());
			this.addPageLayoutNodes(siteAreaLayoutNodeChildren, this.getSiteAreaLayout().getRows());
		}
		return jsonNode;
	}
	
	private void addPageLayoutNodes(ObjectNode pageLayoutNode, LayoutRow[] rows) {
		int rowCount = 0;
		for (LayoutRow row : rows) {
			ObjectNode rowNode = JsonUtils.createObjectNode();
			ObjectNode rowNodeProperties = JsonUtils.createObjectNode();
			ObjectNode rowNodeChildren = JsonUtils.createObjectNode();
			pageLayoutNode.set("row" + rowCount++, rowNode);
			rowNode.set("properties", rowNodeProperties);
			rowNode.set("children", rowNodeChildren);
			rowNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:layoutRow");
			
			int columnCount = 0;
			for (LayoutColumn column : row.getColumns()) {
				ObjectNode columnNode = JsonUtils.createObjectNode();
				ObjectNode columnNodeProperties = JsonUtils.createObjectNode();
				ObjectNode columnNodeChildren = JsonUtils.createObjectNode();
				pageLayoutNode.set("column" + columnCount++, rowNode);
				columnNode.set("properties", columnNodeProperties);
				columnNode.set("children", columnNodeChildren);
				columnNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:layoutColumn");
				columnNodeProperties.put("bpw:width", column.getWidth());
				this.addResourceViewers(columnNodeChildren, column.getViewers());
			}
		}
	}
	
	private void addSidePaneNode(ObjectNode sidenavNode, ObjectNode sidePaneNodeProperties, SidePane sidenav) {
		sidePaneNodeProperties.put("bpw:isLeft", sidenav.isLeft());
		sidePaneNodeProperties.put("bpw:width", sidenav.getWidth());
		this.addResourceViewers(sidenavNode, sidenav.getViewers());
	}
	
	private void addResourceViewers(ObjectNode sidenavNode, ResourceViewer viewers[]) {
		int viewerCount = 0;
		for (ResourceViewer viewer : viewers) {
			ObjectNode viewerNode = JsonUtils.createObjectNode();
			ObjectNode viewerNodeProperties = JsonUtils.createObjectNode();
			sidenavNode.set("viewer" + viewerCount++, viewerNode);
			viewerNode.set("properties", viewerNodeProperties);
			viewerNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:resourceViewer");

			viewerNodeProperties.put("bpw:renderTemplayeName", viewer.getRenderTemplate());
			viewerNodeProperties.put("bpw:title", viewer.getTitle());
			if (viewer.getContentPath() != null && viewer.getContentPath().length > 0) {
				ArrayNode contentPathArray = JsonUtils.creatArrayNode();
				for (String path : viewer.getContentPath()) {
					contentPathArray.add(path);
				}	
				viewerNodeProperties.set("bpw:contentPath", contentPathArray);
			}
		}
	}
	@Override
	public String toString() {
		return "SiteArea [repository=" + repository + ", workspace=" + workspace + ", nodePath=" + nodePath + ", name="
				+ name + ", siteAreaLayout=" + siteAreaLayout + ", lockOwner=" + lockOwner + ", elements=" + elements
				+ ", properties=" + properties + ", metadata=" + metadata + ", searchData=" + searchData + ", badge="
				+ badge + "]";
	}
}
