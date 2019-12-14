package com.bpwizard.wcm.repo.rest.jcr.model;

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
	private String title;
	private String description;
	private String url;
	private int sorderOrder;
	private String friendlyURL;
	private boolean showOnMenu;
	private String defaultContent;
	private String allowedFileExtension;
	private String allowedArtifactTypes;
	
	private boolean securePage;
	private String siteConfig;
	private String contentAreaLayout;
	private int cacheTTL;

	private String navigationId;
	private String navigationType;
	private String function;
	private String translate;
	private String icon;
	private String classes;
	private boolean exactMatch;
	private boolean externalUrl;
	private boolean openInNewTab;

	private keyValues metadata;
	private SearchData searchData;
	private NavigationBadge badge;
	private SiteAreaLayout siteAreaLayout;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getSorderOrder() {
		return sorderOrder;
	}
	public void setSorderOrder(int sorderOrder) {
		this.sorderOrder = sorderOrder;
	}
	public String getFriendlyURL() {
		return friendlyURL;
	}
	public void setFriendlyURL(String friendlyURL) {
		this.friendlyURL = friendlyURL;
	}
	public boolean isShowOnMenu() {
		return showOnMenu;
	}
	public void setShowOnMenu(boolean showOnMenu) {
		this.showOnMenu = showOnMenu;
	}
	public String getDefaultContent() {
		return defaultContent;
	}
	public void setDefaultContent(String defaultContent) {
		this.defaultContent = defaultContent;
	}
	public String getAllowedFileExtension() {
		return allowedFileExtension;
	}
	public void setAllowedFileExtension(String allowedFileExtension) {
		this.allowedFileExtension = allowedFileExtension;
	}
	public String getAllowedArtifactTypes() {
		return allowedArtifactTypes;
	}
	public void setAllowedArtifactTypes(String allowedArtifactTypes) {
		this.allowedArtifactTypes = allowedArtifactTypes;
	}
	public boolean isSecurePage() {
		return securePage;
	}
	public void setSecurePage(boolean securePage) {
		this.securePage = securePage;
	}
	public String getContentAreaLayout() {
		return contentAreaLayout;
	}
	public void setContentAreaLayout(String contentAreaLayout) {
		this.contentAreaLayout = contentAreaLayout;
	}
	public int getCacheTTL() {
		return cacheTTL;
	}
	public void setCacheTTL(int cacheTTL) {
		this.cacheTTL = cacheTTL;
	}
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
	public String getNavigationId() {
		return navigationId;
	}
	public void setNavigationId(String navigationId) {
		this.navigationId = navigationId;
	}
	public String getNavigationType() {
		return navigationType;
	}
	public void setNavigationType(String navigationType) {
		this.navigationType = navigationType;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String getTranslate() {
		return translate;
	}
	public void setTranslate(String translate) {
		this.translate = translate;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getClasses() {
		return classes;
	}
	public void setClasses(String classes) {
		this.classes = classes;
	}
	public boolean isExactMatch() {
		return exactMatch;
	}
	public void setExactMatch(boolean exactMatch) {
		this.exactMatch = exactMatch;
	}
	public boolean isExternalUrl() {
		return externalUrl;
	}
	public void setExternalUrl(boolean externalUrl) {
		this.externalUrl = externalUrl;
	}
	public boolean isOpenInNewTab() {
		return openInNewTab;
	}
	public void setOpenInNewTab(boolean openInNewTab) {
		this.openInNewTab = openInNewTab;
	}
	public NavigationBadge getBadge() {
		return badge;
	}
	public void setBadge(NavigationBadge badge) {
		this.badge = badge;
	}
	public SiteAreaLayout getSiteAreaLayout() {
		return siteAreaLayout;
	}
	public void setSiteAreaLayout(SiteAreaLayout siteAreaLayout) {
		this.siteAreaLayout = siteAreaLayout;
	}
	public String getSiteConfig() {
		return siteConfig;
	}
	public void setSiteConfig(String siteConfig) {
		this.siteConfig = siteConfig;
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
		properties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:siteArea");
		
		properties.put("bpw:name", this.getName());
		properties.put("bpw:title", StringUtils.hasText(this.getTitle()) ? this.getTitle() : this.getName());
		if (StringUtils.hasText(this.getDescription())) {
			properties.put("bpw:description", this.getDescription());
		}
		properties.put("bpw:url", StringUtils.hasText(this.getUrl()) ? this.getUrl() : this.getName());
		if (StringUtils.hasText(this.getFriendlyURL())) {
			properties.put("bpw:friendlyURL", this.getFriendlyURL());
		}
		properties.put("bpw:sorderOrder", this.getSorderOrder());
		properties.put("bpw:showOnMenu", this.isShowOnMenu());

		if (StringUtils.hasText(this.getDefaultContent())) {
			properties.put("bpw:defaultContent", this.getDefaultContent());
		}
		if (StringUtils.hasText(this.getAllowedArtifactTypes())) {
			String[] artifacts = this.getAllowedArtifactTypes().split(",");
			ArrayNode artifactArray = JsonUtils.creatArrayNode();
			for (String value : artifacts) {
				artifactArray.add(value);
			}
			properties.set("bpw:allowedFileExtension", artifactArray);
		}

		if (StringUtils.hasText(this.getAllowedFileExtension())) {
			String[] files = this.getAllowedFileExtension().split(",");
			ArrayNode fileArray = JsonUtils.creatArrayNode();
			for (String file : files) {
				fileArray.add(file);
			}
			properties.set("bpw:allowedFileExtension", fileArray);
		}

		properties.put("bpw:contentAreaLayout", this.getContentAreaLayout());
		properties.put("bpw:siteConfig", this.getSiteConfig());
		properties.put("bpw:securePage", this.isSecurePage());
		properties.put("bpw:cacheTTL", this.getCacheTTL());

		if (StringUtils.hasText(this.getNavigationId())) {
			properties.put("bpw:navigationId", this.getNavigationId());
		}
		
		if (StringUtils.hasText(this.getNavigationType())) {
			properties.put("bpw:navigationType", this.getNavigationType());
		}
		
		if (StringUtils.hasText(this.getTranslate())) {
			properties.put("bpw:translate", this.getTranslate());
		}
		
		if (StringUtils.hasText( this.getIcon())) {
			properties.put("bpw:function", this.getFunction());
		}
		
		if (StringUtils.hasText(this.getIcon())) {
			properties.put("bpw:icon", this.getIcon());
		}
		
		if (StringUtils.hasText(this.getClasses())) {
			properties.put("bpw:classes", this.getClasses());
		}

		properties.put("bpw:exactMatch", this.isExactMatch());
		properties.put("bpw:externalUrl", this.isExternalUrl());
		properties.put("bpw:openInNewTab", this.isOpenInNewTab());
		
		
		if (this.getMetadata() != null && this.getMetadata().getKeyValues() != null) {
			ObjectNode metaDataNode = JsonUtils.createObjectNode();
			ObjectNode metaDataNodeProperties = JsonUtils.createObjectNode();
			ObjectNode metaDataNodeChildren = JsonUtils.createObjectNode();
			children.set("bpw:metaData", metaDataNode);
			metaDataNode.set("properties", metaDataNodeProperties);
			metaDataNode.set("children", metaDataNodeChildren);
			metaDataNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:keyValues");
			int count = 0;
			for (KeyValue keyValue: this.getMetadata().getKeyValues()) {
				
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
			
			if (StringUtils.hasText(this.getSearchData().getDescription())) {
				searchDataDataNodeProperties.put("description", this.getSearchData().getDescription());
			}
			if (this.getSearchData().getKeywords() != null) {
				String[] keywords = this.getSearchData().getKeywords();
				ArrayNode keywordArray = JsonUtils.creatArrayNode();
				for (String keyword : keywords) {
					keywordArray.add(keyword);
				}				
				searchDataDataNodeProperties.set("keywords", keywordArray);
			}
		}
		
		if (this.getBadge() != null) {
			ObjectNode badgeNode = JsonUtils.createObjectNode();
			ObjectNode badgeNodeProperties = JsonUtils.createObjectNode();
			children.set("bpw:badge", badgeNode);
			badgeNode.set("properties", badgeNodeProperties);
			badgeNodeProperties.put(JcrConstants.JCR_PRIMARY_TYPE, "bpw:navigationBadge");
			
			if (StringUtils.hasText(this.getBadge().getTitle())) {
				badgeNodeProperties.put("bpw:title", this.getBadge().getTitle());
			}
			if (StringUtils.hasText(this.getBadge().getTranslate())) {
				badgeNodeProperties.put("bpw:translate", this.getBadge().getTranslate());
			}
			if (StringUtils.hasText(this.getBadge().getBg())) {
				badgeNodeProperties.put("bpw:bg", this.getBadge().getBg());
			}
			if (StringUtils.hasText(this.getBadge().getFg())) {
				badgeNodeProperties.put("bpw:fg", this.getBadge().getFg());
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
				+ name + ", title=" + title + ", description=" + description + ", url=" + url + ", sorderOrder="
				+ sorderOrder + ", friendlyURL=" + friendlyURL + ", showOnMenu=" + showOnMenu + ", defaultContent="
				+ defaultContent + ", allowedFileExtension=" + allowedFileExtension + ", allowedArtifactTypes="
				+ allowedArtifactTypes + ", securePage=" + securePage + ", siteConfig=" + siteConfig
				+ ", contentAreaLayout=" + contentAreaLayout + ", cacheTTL=" + cacheTTL + ", navigationId="
				+ navigationId + ", navigationType=" + navigationType + ", function=" + function + ", translate="
				+ translate + ", icon=" + icon + ", classes=" + classes + ", exactMatch=" + exactMatch
				+ ", externalUrl=" + externalUrl + ", openInNewTab=" + openInNewTab + ", metadata=" + metadata
				+ ", searchData=" + searchData + ", badge=" + badge + ", siteAreaLayout=" + siteAreaLayout
				+ ", lockOwner=" + lockOwner + "]";
	}
}
